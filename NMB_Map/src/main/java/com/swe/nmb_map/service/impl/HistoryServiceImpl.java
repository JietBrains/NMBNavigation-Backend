package com.swe.nmb_map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swe.nmb_map.entity.History;
import com.swe.nmb_map.service.HistoryService;
import com.swe.nmb_map.mapper.HistoryMapper;
import com.swe.nmb_map.utils.JwtHelper;
import com.swe.nmb_map.utils.Result;
import com.swe.nmb_map.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xavier
 * @description 针对表【history】的数据库操作Service实现
 * @createDate 2025-04-26 21:33:52
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History>
        implements HistoryService{
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public Result save(String token, String name) {
        // 根据 token 查询用户 id
        Integer userId = jwtHelper.getUserId(token).intValue();

        // 检查是否已存在相同的 name 记录
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("name", name); // 匹配 user_id 和 name

        // 查询符合条件的记录
        List<History> existingHistories = historyMapper.selectList(queryWrapper);

        // 如果已存在相同的记录，则直接返回成功
        if (!existingHistories.isEmpty()) {
            return Result.ok(null);
        }

        // 查询当前用户的记录总数
        QueryWrapper<History> countQueryWrapper = new QueryWrapper<>();
        countQueryWrapper.eq("user_id", userId);
        int historyCount = historyMapper.selectCount(countQueryWrapper).intValue();

        // 如果已有 10 条记录，则删除最早的一条记录
        if (historyCount >= 10) {
            QueryWrapper<History> deleteQueryWrapper = new QueryWrapper<>();
            deleteQueryWrapper.eq("user_id", userId)
                    .orderByAsc("create_date") // 按创建时间升序排序
                    .last("LIMIT 1");         // 只删除最早的一条记录
            historyMapper.delete(deleteQueryWrapper);
        }

        // 插入新记录
        History history = new History();
        history.setUserId(userId);
        history.setName(name);
        history.setCreateDate(new Date());
        historyMapper.insert(history);

        // 返回成功
        return Result.ok(null);
    }

    @Override
    public Result getAll(String token) {
        // 根据 token 查询用户 id
        int userId = jwtHelper.getUserId(token).intValue();

        // 构造查询条件
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_deleted", 0);

        // 查询所有符合条件的记录
        List<History> historyList = historyMapper.selectList(queryWrapper);

        // 提取 name 字段值并过滤掉 null 值
        List<String> names = historyList.stream()
                .map(History::getName)       // 获取每个 History 对象的 name 字段值
                .toList();                   // 转换为不可变列表

        // 返回结果
        return Result.ok(names);
    }

    @Override
    public Result delete(String token) {
        int uerId = jwtHelper.getUserId(token).intValue();
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", uerId);
        historyMapper.delete(queryWrapper);
        return Result.ok(null);
    }
}




