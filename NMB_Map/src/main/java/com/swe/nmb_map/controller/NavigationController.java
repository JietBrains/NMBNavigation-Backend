package com.swe.nmb_map.controller;

import com.swe.nmb_map.utils.CoordinateEnum;
import com.swe.nmb_map.utils.PythonScriptCaller;
import com.swe.nmb_map.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("navigation")
@CrossOrigin
public class NavigationController {

    @Autowired
    private PythonScriptCaller pythonScriptCaller;

    @GetMapping("target")
    public Result searchTarget(String param1,  String param2) throws Exception {
        // 调用 Python 脚本并获取返回值
        Result<List<Map<String, Object>>> data = pythonScriptCaller.callPythonScript("search", param1, param2);
        // 返回成功结果
        return data;
    }

    @GetMapping("nearest")
    public Result searchNearest(String param1, int status) throws Exception {
        String param2 = status == 0 ? "toilet" : "vendingMachine";
        // 调用 Python 脚本并获取返回值
        Result<List<Map<String, Object>>> data = pythonScriptCaller.callPythonScript("nearest", param1, param2);
        // 返回成功结果
        return data;
    }

    @GetMapping("touch")
    public Result searchTouch(String floor, String x, String y) throws Exception {
        // 调用 Python 脚本并获取返回值
        Result<List<Map<String, Object>>> data = pythonScriptCaller.callPythonScript(floor, x, y);
        // 返回
        return data;
    }

    @GetMapping("getCenter")
    public Result getCenter(String floor) throws Exception {
        CoordinateEnum coordinateEnum = CoordinateEnum.valueOf(floor);
        Map<String, Integer> map  = Map.of("x", coordinateEnum.getX(), "y", coordinateEnum.getY());
        return Result.ok(map);
    }


}
