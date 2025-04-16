package com.swe.nmb_map.controller;


import com.swe.nmb_map.entity.Node;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.service.FavoriteService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: NMB_Map
 * @description:
 * @author: Xavier
 * @create: 2025-04-13 18:40
 **/
@RestController
@RequestMapping("favorite")
@CrossOrigin
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("add")
    public Result add(@RequestHeader String token, @RequestBody Node node) {
        Result result = favoriteService.add(token, node);
        return result;
    }
}
