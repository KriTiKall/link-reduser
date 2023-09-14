package com.example.link.reduce.service;

import com.example.link.reduce.data.entity.LinkEntity;
import com.example.link.reduce.data.entity.StatisticEntity;
import com.example.link.reduce.data.repository.LinkRepository;
import com.example.link.reduce.data.repository.StatRepository;
import com.example.link.reduce.data.repository.UserRepository;
import com.example.link.reduce.model.dto.LinkStat;
import com.example.link.reduce.model.interfaces.IStatService;
import lombok.val;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Service
public class StatService implements IStatService {

    @Autowired
    private StatRepository statRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<LinkStat> getTopOfLink(String login) {
        val user = userRepository.findByLogin(login);
        AtomicInteger counter = new AtomicInteger(1);

        return linkRepository.findByUserId(user.get().getId()).stream()
                .map(this::map)
                .sorted((p1, p2) -> p2.getCount().compareTo(p1.getCount()))
                .limit(20)
                .peek(it -> it.setNumber(counter.getAndIncrement()))
                .toList();
    }

    private LinkStat map(LinkEntity entity) {
        return new LinkStat(0,
                entity.getName(),
                entity.getSite(),
                entity.getLink(),
                statRepository.countByLinkEntity(entity)
        );
    }

    private final String FORMAT_MIN = "yyyy.MM.dd HH:mm";
    private final String FORMAT_HOUR = "yyyy.MM.dd HH";
    private final String FORMAT_DAY = "yyyy.MM.dd";

    private final String MIN = "min";
    private final String HOUR = "hour";
    private final String DAY = "day";

    private final Function<String, RegularTimePeriod> FUNCTION_MIN = it -> new Minute(mapMin(it));
    private final Function<String, RegularTimePeriod> FUNCTION_HOUR = it -> new Hour(mapHour(it));
    private final Function<String, RegularTimePeriod> FUNCTION_DAY = it -> new Day(mapDay(it));


    @Override
    public byte[] getChart(String login, String linkName, String type) {
        val user = userRepository.findByLogin(login);

        if (user.isEmpty())
            return null;

        val entity = linkRepository.findByUserIdAndName(user.get().getId(), linkName);
        val list = statRepository.findAllByLinkEntity(entity).stream()
                .map(StatisticEntity::getClickTime)
                .toList();

        Optional<LocalDateTime> min = list.stream().min((p1, p2) -> p2.compareTo(p1));

        if (min.isEmpty())
            return null;

        JFreeChart chert;
        try {
            String format;
            var function = FUNCTION_MIN;
            switch (type) {
                case MIN -> {
                    format = FORMAT_MIN;
                    function = FUNCTION_MIN;
                }
                case HOUR -> {
                    format = FORMAT_HOUR;
                    function = FUNCTION_HOUR;
                }
                case DAY -> {
                    format = FORMAT_DAY;
                    function = FUNCTION_DAY;
                }
                default -> format = "";
            }

            chert = buildChart(type, format, list, function);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        try {
            ImageIO.write(chert.createBufferedImage(600, 800), "png", bas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bas.toByteArray();
    }

    private JFreeChart buildChart(String type, String format, List<LocalDateTime> list, Function<String, RegularTimePeriod> function) throws ParseException {
        return createChart(
                createDataset(
                        groupByFormat(
                                list,
                                DateTimeFormatter.ofPattern(format)
                        ),
                        function
                ),
                type,
                new SimpleDateFormat(format.substring(5))
        );
    }


    private JFreeChart createChart(final XYDataset dataset, String type, SimpleDateFormat format) {
        format.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Statistics by " + type,
                "Time",
                "Visitors",
                dataset,
                true,
                true,
                false
        );
        var plot = (XYPlot) chart.getPlot();

        XYLineAndShapeRenderer r = (XYLineAndShapeRenderer) plot.getRenderer();

        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(format);
        domain.setVerticalTickLabels(true);

        return chart;
    }

    private XYDataset createDataset(Map<String, Long> data, Function<String, RegularTimePeriod> function) {
        TimeSeries series = new TimeSeries("Visitors");
        val sets = data.entrySet();
//                .stream().sorted((p1, p2)->p2.getKey()..compareTo(p1))

        for (Map.Entry<String, Long> it : sets) {
            series.add(function.apply(it.getKey()), it.getValue());
        }

        return new TimeSeriesCollection(series);
    }

    private Date mapMin(String dateTime) {
        return mapWithFormat(dateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    private Date mapHour(String dateTime) {
        return mapWithFormat(dateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd HH"));
    }

    private Date mapDay(String dateTime) {
        var format = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return Date.from(
                LocalDate.parse(
                                dateTime, format
                        ).atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    private Date mapWithFormat(String dateTime, DateTimeFormatter format) {
        return Date.from(
                LocalDateTime.parse(
                                dateTime, format
                        ).atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    //var format = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"); // "yyyy.MM.dd HH" "yyyy.MM.dd"
    private Map<String, Long> groupByFormat(List<LocalDateTime> list, DateTimeFormatter format) {
        Map<String, Long> data = new HashMap<>();

        for (LocalDateTime localDateTime : list) {
            var temp = localDateTime.format(format);
            if (data.containsKey(temp)) {
                data.put(temp, data.get(temp) + 1);
            } else
                data.put(temp, 1l);
        }

        return data;
    }
}
