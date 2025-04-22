package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName images
 */
@TableName(value ="images")
@Data
public class Images {
    @TableId(type = IdType.AUTO)
    private Integer imageId;

    private String image;

    private String url;
}