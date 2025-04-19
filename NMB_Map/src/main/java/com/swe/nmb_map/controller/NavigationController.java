package com.swe.nmb_map.controller;

import com.swe.nmb_map.utils.PythonScriptCaller;
import com.swe.nmb_map.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("navigation")
@CrossOrigin
public class NavigationController {

    @Autowired
    private PythonScriptCaller pythonScriptCaller;

    @GetMapping("target")
    public Result searchTarget(String param1,  String param2) throws Exception {
        String res = pythonScriptCaller.callPythonScript("search", param1, param2);
        return Result.ok(res);
    }

    @GetMapping("nearest")
    public Result searchNearest(String param1, int status) throws Exception {
        String param2 = status == 0 ? "toilet" : "vendingMachine";
        String res = pythonScriptCaller.callPythonScript("nearest", param1, param2);
        return Result.ok(res);
    }



}
