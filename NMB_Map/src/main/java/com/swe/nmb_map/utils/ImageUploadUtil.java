package com.swe.nmb_map.utils;

import com.swe.nmb_map.entity.Images;
import com.swe.nmb_map.mapper.ImagesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class ImageUploadUtil {

    private static final String IMAGE_UPLOAD_URL = "http://8.140.200.27:5000/upload";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ImagesMapper imagesMapper;

    /**
     * 处理图片上传逻辑
     *
     * @param file 前端传来的图片文件
     * @throws IOException 如果文件处理失败
     */
    public void uploadImage(MultipartFile file) throws IOException {
        // Step 1: 调用图床 API 上传图片
        String imageUrl = uploadToImageBed(file);

        // Step 2: 将图片文件名与 URL 存储到数据库
        saveImageToDatabase(file.getOriginalFilename(), imageUrl);
    }

    /**
     * 调用图床 API 上传图片
     *
     * @param file 图片文件
     * @return 图床返回的图片 URL
     * @throws IOException 如果文件处理失败
     */
    private String uploadToImageBed(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // 确保文件名正确传递
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                IMAGE_UPLOAD_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("url")) {
                return (String) responseBody.get("url");
            }
        }

        throw new RuntimeException("Failed to upload image to image bed.");
    }

    /**
     * 将图片信息存储到数据库
     *
     * @param fileName 图片文件名
     * @param imageUrl 图片 URL
     */
    private void saveImageToDatabase(String fileName, String imageUrl) {
        Images image = new Images();
        image.setImage(fileName);
        image.setUrl(imageUrl);

        imagesMapper.insert(image);
    }
}