package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author azure
* @description 针对表【user】的数据库操作Service
* @createDate 2025-04-09 12:01:40
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result regist(User user);

    Result checkUserName(String username);

    Result getUserInfo(String token);
}
