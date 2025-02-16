package com.blog.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.blog.model.ResultResponse;
import com.blog.model.dto.WeatherDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/weather")
public class WeatherController {
    public static final String apiKey = "XXXXXXXX";//高德地图的天气API KEY

    @GetMapping("/weather")
    public ResultResponse<WeatherDto> weather(HttpServletRequest request) {
        JSONObject jsonObject;

        String url = "https://restapi.amap.com/v3/ip?key=" + apiKey;
        Map<String, Object> params = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> http = new HttpEntity<>(params, headers);

        try {
            log.info("==调用远程方法开始==");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, http, String.class);
            jsonObject = JSONObject.parseObject(response.getBody());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return ResultResponse.error("error", null);
        }finally {
            log.info("==调用远程方法结束==");
        }

        String status = jsonObject.getString("status");
        if(!status.equals("1")) ResultResponse.error("error");
        String adcode = jsonObject.getString("adcode");
        url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + apiKey + "&city=" + adcode;

        try {
            log.info("==调用远程方法开始==");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, http, String.class);
            jsonObject = JSONObject.parseObject(response.getBody());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return ResultResponse.error("error", null);
        }finally {
            log.info("==调用远程方法结束==");
        }

        status = jsonObject.getString("status");
        if(!status.equals("1")) ResultResponse.error("error");
        List<JSONObject> list= jsonObject.getList("lives", JSONObject.class);
        JSONObject jsonItem = list.get(0);
        WeatherDto weather = WeatherDto.builder()
                .province(jsonItem.getString("province"))
                .city(jsonItem.getString("city"))
                .adcode(jsonItem.getString("adcode"))
                .weather(jsonItem.getString("weather"))
                .temperature(jsonItem.getString("temperature"))
                .winddirection(jsonItem.getString("winddirection"))
                .windpower(jsonItem.getString("windpower"))
                .humidity(jsonItem.getString("humidity"))
                .reporttime(jsonItem.getString("reporttime"))
                .temperature_float(jsonItem.getString("temperature_float"))
                .humidity_float(jsonItem.getString("humidity_float"))
                .build();

        return ResultResponse.success("success", weather);
    }
}
