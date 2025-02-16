package com.blog.controller;

import com.alibaba.fastjson2.JSONObject;
import com.blog.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@RestController
@RequestMapping("api/file")
public class FileController {
    private JSONObject saveFile(MultipartFile file, String path) throws IOException {
        String fileName = System.currentTimeMillis() + "." + file.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + path + fileName;
        File writeFile = new File(filePath);
        FileUtils.copyInputStreamToFile(file.getInputStream(), writeFile);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("url", path + fileName);
        json.put("fileName", fileName);
        return json;
    }

    @PostMapping(value = "/uploadCover", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadCover(@RequestPart("file") MultipartFile file, HttpServletResponse resp) throws IOException {
        String path = "/static/cover/";
        JSONObject json = saveFile(file, path);
        resp.getWriter().write(json.toString());
    }

    @PostMapping(value = "/uploadImage", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadImage(@RequestPart("file") MultipartFile file, HttpServletResponse resp) throws IOException {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        String dateStr = year.toString() + "-" + month.toString();
        String path = "/static/image/" + dateStr + "/";
        JSONObject json = saveFile(file, path);
        resp.getWriter().write(json.toString());
    }

    @PostMapping(value = "/uploadHead", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadHead(@RequestPart("file") MultipartFile file, HttpServletResponse resp) throws IOException {
        String path = "/static/head/";
        JSONObject json = saveFile(file, path);
        resp.getWriter().write(json.toString());
    }
}
