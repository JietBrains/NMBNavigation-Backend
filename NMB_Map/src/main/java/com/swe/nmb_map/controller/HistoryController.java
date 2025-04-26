package com.swe.nmb_map.controller;

import com.swe.nmb_map.service.HistoryService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("search")
@CrossOrigin
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @PostMapping("save")
    public Result save(@RequestHeader("Authorization") String token, String name) {
        Result result = historyService.save(token, name);
        return result;
    }

    @GetMapping("get")
    public Result get(@RequestHeader("Authorization") String token) {
        Result result = historyService.getAll(token);
        return result;
    }

    @DeleteMapping("delete")
    public Result delete(@RequestHeader("Authorization") String token) {
        Result result = historyService.delete(token);
        return result;
    }
}
