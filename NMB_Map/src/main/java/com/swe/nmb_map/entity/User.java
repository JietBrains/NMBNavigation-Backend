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
    @TableId
    private Integer userId;

    private String username;

    private String password;

    private String email;

    private Object role;

    private Date registrationTime;
    @TableLogic
    private Integer isDeleted = 0;
}