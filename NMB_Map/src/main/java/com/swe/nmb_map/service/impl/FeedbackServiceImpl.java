package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Feedback;
import com.swe.nmb_map.service.FeedbackService;
import com.swe.nmb_map.mapper.FeedbackMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author xavier
* @description 针对表【feedback】的数据库操作Service实现
* @createDate 2025-04-16 21:44:18
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback>
    implements FeedbackService{

    @Autowired
    private JwtHelper jwtHelper;

    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public Result add(Feedback feedback, String token) {
        //数据装配
        feedback.setCreateTime(new Date());

        //检查登陆状态
        if (jwtHelper.isExpiration(token)) {
            //登陆过期
            return Result.build(null, ResultCodeEnum.NOTLOGIN).message("登陆状态已过期");
        }
        //未过期，放行
        feedbackMapper.insert(feedback);
        return Result.ok(null);
    }
}




