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
    @TableId
    private Integer feedbackId;

    private Integer userId;

    private Integer nodeId;

    private String content;

    private Date createTime;
    @TableLogic
    private Integer isDeleted = 0;
}