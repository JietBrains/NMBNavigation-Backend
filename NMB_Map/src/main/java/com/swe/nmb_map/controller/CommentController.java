package com.swe.nmb_map.controller;

import com.swe.nmb_map.entity.Comment;
import com.swe.nmb_map.service.CollectService;
import com.swe.nmb_map.service.CommentService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("upload")
    public Result comment(@RequestHeader("Authorization") String token, @RequestBody Comment comment) {
        return commentService.comment(token, comment);
    }

    @GetMapping("view")
    public Result view(@RequestHeader("Authorization") String token, String name) {
        return commentService.view(token, name);
    }
}
