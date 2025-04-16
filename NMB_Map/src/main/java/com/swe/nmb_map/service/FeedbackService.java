package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【feedback】的数据库操作Service
* @createDate 2025-04-16 21:44:18
*/
public interface FeedbackService extends IService<Feedback> {

    Result add(Feedback feedback, String token);
}
