package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Node;
import com.swe.nmb_map.service.NodeService;
import com.swe.nmb_map.mapper.NodeMapper;
import org.springframework.stereotype.Service;

/**
* @author azure
* @description 针对表【node】的数据库操作Service实现
* @createDate 2025-04-09 12:01:40
*/
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node>
    implements NodeService{

}




