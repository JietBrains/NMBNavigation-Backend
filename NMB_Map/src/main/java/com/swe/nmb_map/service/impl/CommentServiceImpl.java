package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Comment;
import com.swe.nmb_map.entity.User;
import com.swe.nmb_map.mapper.UserMapper;
import com.swe.nmb_map.service.CommentService;
import com.swe.nmb_map.mapper.CommentMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xavier
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2025-04-17 20:20:28
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService{

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result comment(String token, Comment comment) {
        // 根据token查询用户id
        int userId = jwtHelper.getUserId(token).intValue();
        comment.setUserId(userId);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());

        // 插入记录
        commentMapper.insert(comment);
        return Result.ok(null);
    }

    @Override
    public Result view(String name) {
        // 构造查询条件
        QueryWrapper<Comment> queryWrapper =  new QueryWrapper<>();
        queryWrapper.eq("name", name);

        List<Comment> commentList = commentMapper.selectList(queryWrapper);

        // 转换为指定格式
        List<Map<String, Object>> comments = commentList.stream()
                .map(comment -> {
                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("description", comment.getDescription());

                    // 将 images 转换为目标格式
                    List<String> images = comment.getImages();
                    if (images != null && !images.isEmpty()) {
                        // 使用 StringBuilder 来构建最终字符串
                        StringBuilder imagesStringBuilder = new StringBuilder();
                        for (int i = 0; i < images.size(); i++) {
                            if (i > 0) {
                                imagesStringBuilder.append(", ");
                            }
                            imagesStringBuilder.append(images.get(i));
                        }
                        commentData.put("images", imagesStringBuilder.toString()); // 添加引号
                    } else {
                        commentData.put("images", ""); // 如果为空，返回空字符串
                    }
                    int userId = comment.getUserId();
                    User user = userMapper.selectById(userId);
                    String username = user.getUsername();
                    String avatar = user.getAvatarurl();

                    commentData.put("user", username);
                    commentData.put("avatar", avatar);
                    commentData.put("time", comment.getCreateTime());

                    return commentData;
                })
                .collect(Collectors.toList());

        // 构造返回结果
        Map<String, Object> responseData = new HashMap<>();

        responseData.put("comments", comments);

        return Result.ok(responseData);

    }
}




