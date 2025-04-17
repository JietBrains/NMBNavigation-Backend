package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【comment】的数据库操作Service
* @createDate 2025-04-17 21:01:21
*/
public interface CommentService extends IService<Comment> {

    Result comment(String token, Comment comment);

    Result view(String token, String name);
}
