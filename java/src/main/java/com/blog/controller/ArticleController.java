package com.blog.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.blog.model.ResultResponse;
import com.blog.model.dti.ArticleDti;
import com.blog.model.dto.ArticleDto;
import com.blog.model.dto.ArticleNode;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/article")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @GetMapping("/detail")
    public ResultResponse<ArticleNode> detail(@RequestParam("id") Integer id) {
        ArticleNode article = service.detail(id);
        return ResultResponse.success("success", article);
    }

    @PostMapping("/summary")
    public ResultResponse<ArticleDto> summary(@RequestBody ArticleDti dti) {
        ArticleDto article = service.summary(dti);
        return ResultResponse.success("success", article);
    }

    @PostMapping("/update")
    public ResultResponse update(@RequestBody ArticleDti dti) {
        service.update(dti);
        return ResultResponse.success("success");
    }

    @PostMapping("/publish")
    public ResultResponse publish(@RequestBody ArticleDti dti) {
        service.publish(dti);
        return ResultResponse.success("success");
    }

    @PostMapping("/search")
    public ResultResponse search(@RequestBody ArticleDti dti) {
        ArticleDto article = service.search(dti);
        return ResultResponse.success("success", article);
    }
}
