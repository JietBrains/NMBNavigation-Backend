package com.swe.nmb_map.mapper;

import com.swe.nmb_map.entity.Favorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author azure
* @description 针对表【favorite】的数据库操作Mapper
* @createDate 2025-04-09 12:01:40
* @Entity com.swe.nmb_map.entity.Favorite
*/
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

}




