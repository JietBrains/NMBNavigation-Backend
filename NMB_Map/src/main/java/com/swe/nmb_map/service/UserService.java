package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【user】的数据库操作Service
* @createDate 2025-04-27 00:45:35
*/
public interface UserService extends IService<User> {

    Result login(User user);
}
