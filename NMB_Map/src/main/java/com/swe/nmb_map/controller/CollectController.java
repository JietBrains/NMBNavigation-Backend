package com.swe.nmb_map.controller;


import com.swe.nmb_map.service.CollectService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("collect")
@CrossOrigin
public class CollectController {
    @Autowired
    private CollectService collectService;

    @PostMapping("add")
    public Result add(@RequestHeader("Authorization") String token, String name) {
        Result result =  collectService.add(token, name);
        return result;
    }

    @PostMapping("delete")
    public Result delete(@RequestHeader("Authorization") String token, String name) {
        Result result =  collectService.delete(token, name);
        return result;
    }

    @GetMapping("getAll")
    public Result getAll(@RequestHeader("Authorization") String token) {
        Result result =  collectService.getAll(token);
        return result;
    }
}
