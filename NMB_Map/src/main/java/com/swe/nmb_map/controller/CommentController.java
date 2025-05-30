package com.swe.nmb_map.controller;

import com.swe.nmb_map.entity.Comment;
import com.swe.nmb_map.service.CollectService;
import com.swe.nmb_map.service.CommentService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

//    @PostMapping("upload")
//    public Result comment(@RequestHeader("Authorization") String token, @RequestBody Comment comment) {
//        return commentService.comment(token, comment);
//    }

    @PostMapping("upload")
    public Result comment(@RequestHeader("Authorization") String token,
                          @RequestParam("name") String name,
                          @RequestParam("description") String description,
                          @RequestParam(value = "images", required = false) MultipartFile[] images) {
        // 如果 images 为 null，则创建一个空的 ArrayList
        ArrayList<MultipartFile> imageList = (images == null) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(images));
        return commentService.upload(token, name, description, imageList);
    }

    @GetMapping("view")
    public Result view(String name) {
        return commentService.view(name);
    }
}
