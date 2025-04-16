package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author azure
* @description 针对表【feedback】的数据库操作Service
* @createDate 2025-04-09 12:01:40
*/
public interface FeedbackService extends IService<Feedback> {

    Result add(Feedback feedback, String token);
}
