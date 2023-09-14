package com.example.link.reduce.controller.stat;

import com.example.link.reduce.model.dto.LinkStat;
import com.example.link.reduce.model.interfaces.IStatService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    @Autowired
    private IStatService service;


    @GetMapping("/top")
    public List<LinkStat> getTopOfLink() {
        val login = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getTopOfLink(login);
    }

    @GetMapping(value = "/chart", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getChart(@RequestParam String type, @RequestParam String name) {
        val login = SecurityContextHolder.getContext().getAuthentication().getName();
        val result = service.getChart(login, name, type);

        if (result == null)
            return new byte[0];
        else
            return result;
    }
}
