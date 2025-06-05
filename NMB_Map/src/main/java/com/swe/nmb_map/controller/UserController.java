package com.swe.nmb_map.controller;


import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.entity.Wxuser;
import com.swe.nmb_map.service.UserService;
import com.swe.nmb_map.service.WxuserService;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private WxuserService wxuserService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("login")
    public Result login(@RequestBody Wxuser user) { //请求体类型
        Result result = wxuserService.login(user);
        return result;
    }

//    @PostMapping("login")
//    public Result login(@RequestBody User user) { //请求体类型
//        Result result = userService.login(user);
//        return result;
//    }

//    @PostMapping("checkUserName")
//    public Result checkUserName(String username) { //默认为param形式
//        Result result = userService.checkUserName(username);
//        return result;
//    }
//
//    @PostMapping("regist")
//    public Result regist(@RequestBody User user) {
//        Result result = userService.regist(user);
//        return result;
//    }

    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader(value="Authorization", required = false) String token) {
        if (token == null) {
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        boolean expiration = jwtHelper.isExpiration(token);

        if (expiration) {
            //已经过期
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }

        return Result.ok(null);
    }

    @GetMapping("getInfo")
    public Result userInfo(@RequestHeader("Authorization") String token){
        Result result = wxuserService.getInfo(token);
        return result;
    }

    @PostMapping("updateInfo")
    public Result updateInfo(@RequestHeader("Authorization") String token,
                             String nickName,
                             @RequestHeader("file") MultipartFile file) throws IOException {
        Result result = wxuserService.updateInfo(token, nickName, file);
        System.out.println(nickName);
        return result;
    }
}
