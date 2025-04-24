package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Images;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;

/**
* @author xavier
* @description 针对表【images】的数据库操作Service
* @createDate 2025-04-22 15:46:52
*/
public interface ImagesService extends IService<Images> {

    Result view(String name);
}
