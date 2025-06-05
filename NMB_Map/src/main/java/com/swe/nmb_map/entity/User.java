package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;

    private Date registrationTime;
    @TableLogic
    private Integer isDeleted = 0;

    private String nickname;

    private String avatarurl;
}