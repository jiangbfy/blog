package com.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper extends BaseMapper<UserEntity> {
}
