package com.swe.nmb_map.controller;

import com.swe.nmb_map.utils.PythonScriptCaller;
import com.swe.nmb_map.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
@CrossOrigin
public class searchTest {

    @Autowired
    private PythonScriptCaller pythonScriptCaller;

    @RequestMapping("test")
    public Result test(String param1, String param2, String param3) throws Exception {
        String res = pythonScriptCaller.callPythonScript(param1, param2, param3);
        return Result.ok(res);
    }

}
