package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Favorite;
import com.swe.nmb_map.entity.Node;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.service.FavoriteService;
import com.swe.nmb_map.mapper.FavoriteMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author azure
* @description 针对表【favorite】的数据库操作Service实现
* @createDate 2025-04-09 12:01:40
*/
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite>
    implements FavoriteService{
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Result add(String token, Node node) {
        //根据token查询用户id
        int userId = jwtHelper.getUserId(token).intValue();
        //数据装配
        int nodeId = node.getNodeId();
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setNodeId(nodeId);
        favorite.setCreateTime(new Date());
        favoriteMapper.insert(favorite);

        return Result.ok(null);
    }
}




