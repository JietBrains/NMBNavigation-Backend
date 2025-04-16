package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Feedback;
import com.swe.nmb_map.service.FeedbackService;
import com.swe.nmb_map.mapper.FeedbackMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author azure
* @description 针对表【feedback】的数据库操作Service实现
* @createDate 2025-04-09 12:01:40
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback>
    implements FeedbackService{
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private FeedbackMapper feedbackMapper;
    @Override
    public Result add(Feedback feedback, String token) {
        //根据token查询用户id
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        feedback.setUserId(userId);
        feedback.setCreateTime(new Date());

        feedbackMapper.insert(feedback);

        return Result.ok(null);
    }
}




