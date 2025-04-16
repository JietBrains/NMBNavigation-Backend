package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.entity.Node;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.utils.Result;

/**
* @author azure
* @description 针对表【favorite】的数据库操作Service
* @createDate 2025-04-09 12:01:40
*/
public interface FavoriteService extends IService<Favorite> {

    Result add(String token, Node node);
}
