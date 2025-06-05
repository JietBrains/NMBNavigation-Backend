package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.entity.Wxuser;
import com.swe.nmb_map.service.WxuserService;
import com.swe.nmb_map.mapper.WxuserMapper;
import com.swe.nmb_map.utils.ImageUploadUtil;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author xavier
* @description 针对表【wxuser】的数据库操作Service实现
* @createDate 2025-06-05 20:45:25
*/
@Service
public class WxuserServiceImpl extends ServiceImpl<WxuserMapper, Wxuser>
    implements WxuserService{

    @Autowired
    private WxuserMapper wxuserMapper;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private ImageUploadUtil imageUploadUtil;

    @Override
    public Result login(Wxuser user) {
        // 根据账号和昵称查询数据
        LambdaQueryWrapper<Wxuser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Wxuser::getId, user.getId());
        Wxuser existingUser = wxuserMapper.selectOne(lambdaQueryWrapper);

        if (existingUser == null) {
            // 如果用户不存在，则插入新用户
            wxuserMapper.insert(user);
            // 获取刚插入的用户记录
            existingUser = wxuserMapper.selectOne(lambdaQueryWrapper);
        }

        // 根据用户ID生成token
        String token = jwtHelper.createToken(existingUser.getId());

        // 将token封装到result返回
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return Result.ok(data);
    }

    @Override
    public Result getInfo(String token) {
        String userId = jwtHelper.getUserId(token);
        LambdaQueryWrapper<Wxuser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Wxuser::getId, userId);
        Wxuser user = wxuserMapper.selectOne(lambdaQueryWrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("avatar", user.getAvatar());
        data.put("nickname", user.getNickname());
        return Result.ok(data);
    }

    @Override
    public Result updateInfo(String token, String nickname, MultipartFile file) throws IOException {
        String userId = jwtHelper.getUserId(token);
        LambdaQueryWrapper<Wxuser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Wxuser::getId, userId);
        Wxuser user = wxuserMapper.selectOne(lambdaQueryWrapper);

        user.setNickname(nickname);
        Map<String, Object> data = new HashMap<>();
        data.put("nickname", user.getNickname());

        user.setNickname(nickname);
        if (file != null) {
            String avatar = imageUploadUtil.uploadAndGetUrl(file);
            user.setAvatar(avatar);
        }
        wxuserMapper.updateById(user);
        data.put("avatar", user.getAvatar());


        return Result.ok(data);
    }

}




