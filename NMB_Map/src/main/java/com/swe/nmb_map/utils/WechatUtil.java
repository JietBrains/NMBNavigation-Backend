package com.swe.nmb_map.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
    public class WechatUtil {

    @Value("${wx.appid}")
    private String APPID;

    @Value("${wx.secret}")
    private String SECRET;

    @Autowired
    private  RestTemplate restTemplate;


    // 调用微信接口获取openid
    public String getOpenidByCode(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                APPID, SECRET, code);
        ResponseEntity<WechatResponse> response = restTemplate.getForEntity(url, WechatResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            WechatResponse data = response.getBody();
            if (data.getErrcode() != null) {
                throw new RuntimeException("微信接口错误: " + data.getErrmsg());
            }
            return data.getOpenid();
        } else {
            throw new RuntimeException("微信接口调用失败");
        }
    }

    @Data
    public static class WechatResponse {
        private String openid;
        private String session_key;
        private Integer errcode;
        private String errmsg;
    }
}
