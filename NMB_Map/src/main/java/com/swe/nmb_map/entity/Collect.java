package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect {
    @TableId(type = IdType.AUTO)
    private Integer collectId;

    private Integer userId;

    private String collectObj;

    private Integer top;

    private Date createTime;
}