package com.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.model.entity.ArticleEntity;
import org.springframework.stereotype.Component;

@Component
public interface ArticleMapper extends BaseMapper<ArticleEntity> {
}
