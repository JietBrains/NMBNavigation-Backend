package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【collect】的数据库操作Service
* @createDate 2025-04-26 20:40:09
*/
public interface CollectService extends IService<Collect> {
    Result add(String Authorization, String name);

    Result delete(String Authorization, String name);

    Result getAll(String token);

    Result alterTop(String token, String name);

    Result judgement(String token, String name);
}
