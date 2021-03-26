package com.jongtix.zerock.controller;

import com.jongtix.zerock.service.bunjang.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping("/api/v1/event-summaries")
    public String getList(@Nullable String sort) {
        if (sort == null || "".equals(sort)) {
            sort = "Total";
        }
        return resultService.getList(sort);
    }

}
