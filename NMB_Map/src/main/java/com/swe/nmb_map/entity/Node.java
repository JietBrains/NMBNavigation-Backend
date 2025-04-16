package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName node
 */
@TableName(value ="node")
@Data
public class Node {
    @TableId
    private Integer nodeId;

    private String nodeName;

    private String nodeType;

    private Integer floor;
    @TableLogic
    private Integer isDeleted = 0;
}