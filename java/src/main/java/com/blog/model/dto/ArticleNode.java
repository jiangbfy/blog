package com.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleNode {
    private Integer id;
    private String title;
    private String content;
    private List<String> tags;
    private String cover;
    private String updateTime;
}
