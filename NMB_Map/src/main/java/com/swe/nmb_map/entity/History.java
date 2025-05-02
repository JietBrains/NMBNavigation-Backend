package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @TableName history
 */
@TableName(value ="history")
@Data
public class History {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String name;
    @TableLogic
    private Integer isDeleted = 0;

    private Date createDate;
}