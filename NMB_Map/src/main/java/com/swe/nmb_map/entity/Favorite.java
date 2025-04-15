package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * @TableName favorite
 */
@TableName(value ="favorite")
@Data
public class Favorite {
    @TableId
    private Integer favoriteId;

    private Integer userId;

    private Integer nodeId;

    private Date createTime;
    @TableLogic
    private Integer isDeleted = 0;
}