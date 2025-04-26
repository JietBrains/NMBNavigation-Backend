package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @TableName history
 */
@TableName(value ="history")
@Data
public class History {
    private Integer id;

    private Integer userId;

    private String name;

    private Integer isDeleted;

    private Date createDate;
}