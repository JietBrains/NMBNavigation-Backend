package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect {
    @TableId
    private Integer collectId;

    private Integer userId;

    private String collectObj;
}