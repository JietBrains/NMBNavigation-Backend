package com.swe.nmb_map.controller;


import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.service.UserService;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: NMB_Map
 * @description:
 * @author: Xavier
 * @create: 2025-04-09 16:51
 **/
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("login")
    public Result login(@RequestBody User user) { //请求体类型
        Result result = userService.login(user);
        return result;
    }

    @PostMapping("checkUserName")
    public Result checkUserName(String username) { //默认为param形式
        Result result = userService.checkUserName(username);
        return result;
    }

    @PostMapping("regist")
    public Result regist(@RequestBody User user) {
        Result result = userService.regist(user);
        return result;
    }

    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader("Authorization") String token) {

        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //已经过期
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        return Result.ok(null);
    }

    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader("Authorization") String token){
        Result result = userService.getUserInfo(token);
        return result;
    }
}
