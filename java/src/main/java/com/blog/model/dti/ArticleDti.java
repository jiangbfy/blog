package com.blog.model.dti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDti {
    private Integer id;
    private String title;
    private String content;
    private List<String> tags;
    private String cover;
    private Integer page;
    private Integer pageSize;
}
