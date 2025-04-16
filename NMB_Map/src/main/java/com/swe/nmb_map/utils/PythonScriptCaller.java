package com.swe.nmb_map.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonScriptCaller {
    public String callPythonScript(int param1, String param2) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
            "python3", "path/to/your_script.py", "{\"param1\": " + param1 + ", \"param2\": \"" + param2 + "\"}"
        );
        Process process = processBuilder.start();

        // 读取 Python 脚本的输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return output.toString(); // 返回结果
        } else {
            throw new RuntimeException("Python script failed with exit code " + exitCode);
        }
    }
}