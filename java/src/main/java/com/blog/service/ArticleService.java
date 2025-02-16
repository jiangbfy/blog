package com.blog.service;

import com.blog.model.dti.ArticleDti;
import com.blog.model.dto.ArticleDto;
import com.blog.model.dto.ArticleNode;

import java.util.List;

public interface ArticleService {
    public ArticleNode detail(Integer id);
    public boolean publish(ArticleDti article);
    public boolean update(ArticleDti article);
    public ArticleDto summary(ArticleDti dti);
    public ArticleDto search(ArticleDti dti);
}
