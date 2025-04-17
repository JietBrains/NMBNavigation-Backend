package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.Collect;
import com.swe.nmb_map.entity.Comment;
import com.swe.nmb_map.mapper.CommentMapper;
import com.swe.nmb_map.service.CollectService;
import com.swe.nmb_map.mapper.CollectMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
* @author xavier
* @description 针对表【collect】的数据库操作Service实现
* @createDate 2025-04-17 16:32:44
*/
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect>
    implements CollectService{
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Result add(String Authorization, String name) {
        // 根据token查询用户id
        int userId = jwtHelper.getUserId(Authorization).intValue();

        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setCollectObj(name);
        try {
            // 插入记录
            collectMapper.insert(collect);
            return Result.ok(null);
        } catch (DuplicateKeyException e) {
            // 捕获唯一约束冲突异常
            return Result.build(null, ResultCodeEnum.FAIL).message("该收藏已存在");
        }
    }

    @Override
    public Result delete(String Authorization, String name) {
        // 根据token查询用户id
        int userId = jwtHelper.getUserId(Authorization).intValue();
        // 构造删除条件
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId); // 匹配 user_id
        queryWrapper.eq("collect_obj", name); // 匹配 collect_obj
        // 执行删除
        int rows = collectMapper.delete(queryWrapper);

        if (rows > 0) {
            return Result.ok(null).message("删除成功");
        } else {
            return Result.build(null, ResultCodeEnum.FAIL).message("删除失败");
        }
    }

    @Override
    public Result getAll(String token) {
        // 根据token查询用户id
        int userId = jwtHelper.getUserId(token).intValue();
        // 构造删除条件
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId); // 匹配 user_id

        // 查询所有符合条件的收藏记录
        List<Collect> collectList = collectMapper.selectList(queryWrapper);

        // 提取 collect_obj 字段值
        List<String> collects = collectList.stream()
                .map(Collect::getCollectObj) // 获取每个 Collect 对象的 collect_obj 字段值
                .filter(Objects::nonNull)    // 过滤掉 null 值
                .toList();                   // 转换为不可变列表

        // 返回结果
        Map<String, List<String>> collectInfo = Map.of("collects", collects);
        return Result.ok(collectInfo);

    }


}




