package com.swe.nmb_map.service.impl;

import com.swe.nmb_map.utils.MD5Util;
import com.swe.nmb_map.utils.ResultCodeEnum;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.service.UserService;
import com.swe.nmb_map.mapper.UserMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author azure
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-04-09 12:01:40
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Result login(User user) {
        //根据账号查询数据
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(lambdaQueryWrapper);

        //账号错误
        if (loginUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //对比密码
        if (!StringUtils.isEmpty(user.getPassword())
                && MD5Util.encrypt(user.getPassword()).equals(loginUser.getPassword())) {
            //登录成功
            //根据用户ID生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUserId()));
            //将token封装到result返回-jwt
            Map data = new HashMap<>();
            data.put("token",token);
            return Result.ok(data);
        }
        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) { //已被使用
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        user.setPassword(MD5Util.encrypt(user.getPassword()));
        userMapper.insert(user);

        return Result.ok(null);
    }

    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(lambdaQueryWrapper);

        if (count == 0) {
            return Result.ok(null);
        }

        return Result.build(null, ResultCodeEnum.USERNAME_USED);
    }

    @Override
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询数据
        User user = userMapper.selectById(userId);

        if (user != null) {
            user.setPassword(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }
}




