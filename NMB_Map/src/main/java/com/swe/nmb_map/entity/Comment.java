package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment", autoResultMap = true)
@Data
public class Comment {
    private Integer id;

    private String name;

    @TableField(typeHandler = JacksonTypeHandler.class) // 使用 JacksonTypeHandler 处理 JSON    private List<String> images; // 使用 JacksonTypeHandler
    private List<String> images; // 使用 JacksonTypeHandler

    private Date createTime;

    private Date updateTime;

    private Integer userId;

    private String description;
}