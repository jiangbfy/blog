package com.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class UserEntity {
    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "password")
    private String password;

    @TableField(value = "headUrl")
    private String headUrl;

    @TableField(value = "role")
    private Integer role;
}
