package com.swe.nmb_map.controller;

import com.swe.nmb_map.service.ImagesService;
import com.swe.nmb_map.utils.ImageUploadUtil;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageUploadUtil imageUploadUtil;
    @Autowired
    private ImagesService imagesService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestHeader("file") MultipartFile file) {
        try {
            imageUploadUtil.uploadImage(file);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/view")
    public Result viewImage(String name) {
        Result result = imagesService.view(name);
        return result;
    }
}