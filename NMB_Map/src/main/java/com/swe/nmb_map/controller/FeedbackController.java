package com.swe.nmb_map.controller;


import com.swe.nmb_map.entity.Feedback;
import com.swe.nmb_map.service.FeedbackService;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: NMB_Map
 * @description:
 * @author: Xavier
 * @create: 2025-04-09 21:43
 **/
@RestController
@RequestMapping("feedback")
@CrossOrigin
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("add")
    public Result addFeedback(@RequestBody Feedback feedback, @RequestHeader String token) {
        Result result = feedbackService.add(feedback, token);
        return result;
    }
}
