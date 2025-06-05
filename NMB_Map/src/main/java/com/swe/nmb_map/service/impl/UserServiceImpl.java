package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.service.UserService;
import com.swe.nmb_map.mapper.UserMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author xavier
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-04-27 00:45:35
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
        // 根据账号和昵称查询数据
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickname, user.getNickname());
        User existingUser = userMapper.selectOne(lambdaQueryWrapper);

        if (existingUser == null) {
            // 如果用户不存在，则插入新用户
            user.setRegistrationTime(new Date());
            userMapper.insert(user);
            // 获取刚插入的用户记录
            existingUser = userMapper.selectOne(lambdaQueryWrapper);
        }

        // 根据用户ID生成token
//        String token = jwtHelper.createToken(Long.valueOf(existingUser.getUserId()));

        // 将token封装到result返回
        Map<String, Object> data = new HashMap<>();
//        data.put("token", token);
        return Result.ok(data);
    }
}




