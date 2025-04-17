package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * @TableName feedback
 */
@TableName(value ="feedback")
@Data
public class Feedback {
    @TableId(type = IdType.AUTO)
    private Integer feedbackId;

    private String description;

    private Date createTime;

    @TableLogic
    private Integer isDeleted = 0;

    private String phone;

    private String email;

    private Object images;
}