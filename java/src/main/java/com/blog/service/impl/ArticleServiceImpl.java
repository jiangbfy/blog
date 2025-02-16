package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dao.ArticleMapper;
import com.blog.model.dti.ArticleDti;
import com.blog.model.dto.ArticleDto;
import com.blog.model.dto.ArticleNode;
import com.blog.model.entity.ArticleEntity;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ArticleNode detail(Integer id) {
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        ArticleEntity entity = articleMapper.selectOne(queryWrapper);
        String[] items = entity.getTags().split(",");
        List<String> tags = Arrays.asList(items);
        ArticleNode article = ArticleNode.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .cover(entity.getCover())
                .title(entity.getTitle())
                .tags(tags)
                .build();
        return article;
    }

    @Override
    public boolean publish(ArticleDti article) {
        List<String> tagsList = article.getTags();
        String tags = "";
        if (tagsList.size() > 0) {
            for (String item : tagsList) {
                tags += item + ",";
            }
        }
        ArticleEntity entity = new ArticleEntity();
        entity.setContent(article.getContent());
        entity.setCover(article.getCover());
        entity.setTitle(article.getTitle());
        entity.setTags(tags);
        articleMapper.insert(entity);
        return true;
    }

    @Override
    public boolean update(ArticleDti article) {
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", article.getId());
        ArticleEntity entity = new ArticleEntity();

        List<String> tagsList = article.getTags();
        String tags = "";
        if (tagsList.size() > 0) {
            for (String item : tagsList) {
                tags += item + ",";
            }
        }
        entity.setTags(tags);
        entity.setTitle(article.getTitle());
        entity.setCover(article.getCover());
        entity.setContent(article.getContent());
        articleMapper.update(entity, queryWrapper);
        return true;
    }

    @Override
    public ArticleDto summary(ArticleDti dti) {
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "tags", "cover", "updateTime");
        queryWrapper.orderByDesc("createTime");
        Page<ArticleEntity> pageObj = new Page<>(dti.getPage(), dti.getPageSize());
        IPage<ArticleEntity> IPage = articleMapper.selectPage(pageObj, queryWrapper);
        Long total = IPage.getTotal();
        List<ArticleEntity> listEntity = IPage.getRecords();
        List<ArticleNode> list = new ArrayList<>();
        ArticleDto dto = new ArticleDto();
        for (ArticleEntity item: listEntity) {
            String[] arr = item.getTags().split(",");
            List<String> tagsList = Arrays.asList(arr);
            ArticleNode node = ArticleNode.builder()
                    .id(item.getId())
                    .tags(tagsList)
                    .title(item.getTitle())
                    .cover(item.getCover())
                    .updateTime(item.getUpdateTime())
                    .build();
            list.add(node);
        }
        dto.setList(list);
        dto.setPage(dti.getPage());
        dto.setPageSize(dti.getPageSize());
        dto.setTotal(total);
        return dto;
    }

    @Override
    public ArticleDto search(ArticleDti dti) {
        String likeStr = "%" + dti.getTitle() + "%";
        QueryWrapper<ArticleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title", "tags", "cover", "updateTime");
        queryWrapper.like("title", likeStr);
        queryWrapper.orderByDesc("createTime");
        Page<ArticleEntity> pageObj = new Page<>(dti.getPage(), dti.getPageSize());
        IPage<ArticleEntity> IPage = articleMapper.selectPage(pageObj, queryWrapper);
        Long total = IPage.getTotal();
        List<ArticleEntity> listEntity = IPage.getRecords();
        List<ArticleNode> list = new ArrayList<>();
        ArticleDto dto = new ArticleDto();
        for (ArticleEntity item: listEntity) {
            String[] arr = item.getTags().split(",");
            List<String> tagsList = Arrays.asList(arr);
            ArticleNode node = ArticleNode.builder()
                    .id(item.getId())
                    .tags(tagsList)
                    .title(item.getTitle())
                    .cover(item.getCover())
                    .updateTime(item.getUpdateTime())
                    .build();
            list.add(node);
        }
        dto.setList(list);
        dto.setPage(dti.getPage());
        dto.setPageSize(dti.getPageSize());
        dto.setTotal(total);
        return dto;
    }
}
