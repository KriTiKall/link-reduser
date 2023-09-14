package com.example.link.reduce.model.interfaces;

import com.example.link.reduce.model.dto.LinkStat;

import java.text.ParseException;
import java.util.List;

public interface IStatService {

    List<LinkStat> getTopOfLink(String login);

    byte[] getChart(String login, String linkName, String type);
}
