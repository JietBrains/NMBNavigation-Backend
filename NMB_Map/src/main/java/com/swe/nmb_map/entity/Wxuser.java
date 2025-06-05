package com.swe.nmb_map.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName wxuser
 */
@TableName(value ="wxuser")
@Data
public class Wxuser {
    @TableId
    private String id;

    private String nickname;

    private String avatar;
}