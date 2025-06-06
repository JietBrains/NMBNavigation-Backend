package com.swe.nmb_map.service;

import com.swe.nmb_map.entity.Wxuser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swe.nmb_map.utils.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author xavier
* @description 针对表【wxuser】的数据库操作Service
* @createDate 2025-06-05 20:45:25
*/
public interface WxuserService extends IService<Wxuser> {
//    Result login(@RequestBody Wxuser user);

    Result login(@RequestHeader String code);

    Result getInfo(String token);

    Result updateInfo(String token, String nickname, MultipartFile file) throws IOException;
}