package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.History;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【history】的数据库操作Service
* @createDate 2025-04-26 21:41:20
*/
public interface HistoryService extends IService<History> {

    Result save(String token, String name);

    Result getAll(String token);
}
