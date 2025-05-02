package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Images;
import com.swe.nmb_map.service.ImagesService;
import com.swe.nmb_map.mapper.ImagesMapper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author xavier
* @description 针对表【images】的数据库操作Service实现
* @createDate 2025-04-22 15:46:52
*/
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images>
    implements ImagesService{

    private final ImagesMapper imagesMapper;

    public ImagesServiceImpl(ImagesMapper imagesMapper) {
        this.imagesMapper = imagesMapper;
    }

    @Override
    public Result view(String name) {
        QueryWrapper<Images> query = new QueryWrapper<>();
        query.eq("image", name + ".png");
        Images images = imagesMapper.selectOne(query);

        if (images == null) {
            return Result.build(null, ResultCodeEnum.FAIL);
        } else {
            return Result.ok(images.getUrl());
        }
    }

    @Override
    public Result search(String name) {
        QueryWrapper<Images> query = new QueryWrapper<>();
        query.eq("image", name + ".png");
        Images target = imagesMapper.selectOne(query);

        String floorName = name.substring(0, 2);
        QueryWrapper<Images> newQuery = new QueryWrapper<>();
        newQuery.eq("image", floorName + ".png");
        Images floor = imagesMapper.selectOne(newQuery);

        Map<String, Object> map = new HashMap<>();
        map.put("target", target == null ? "" : target.getUrl());
        map.put("floor", floor.getUrl());
        return Result.ok(map);
    }
}




