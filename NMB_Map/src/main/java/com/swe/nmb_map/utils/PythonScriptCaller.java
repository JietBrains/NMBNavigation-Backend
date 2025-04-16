package com.swe.nmb_map.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

@Component
public class PythonScriptCaller {

    private Process process; // 保存 Python 进程
    private BufferedWriter writer; // 用于向 Python 发送数据
    private BufferedReader reader; // 用于读取 Python 输出

    /**
     * 启动 Python 脚本进程。
     */
    @PostConstruct
    public void init() throws Exception {
        // 启动 Python 脚本
        ProcessBuilder processBuilder = new ProcessBuilder(
                "/Users/xavier/IdeaProjects/NMBNavigation-Backend/NMB_Map/myenv/bin/python",
                "src/algorithm/main.py" // 指定脚本路径
        );
        process = processBuilder.start();

        // 初始化输入和输出流
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        System.out.println("Python script started.");
    }

    /**
     * 调用 Python 脚本并返回结果。
     *
     * @param param1 第一个参数
     * @param param2 第二个参数
     * @param param3 第三个参数
     * @return Python 脚本的输出字符串
     * @throws Exception 如果通信失败或读取输出时出错
     */
    public String callPythonScript(String param1, String param2, String param3) throws Exception {

        // 检查 Python 进程是否启动成功
        if (process.isAlive()) {
            System.out.println("Python process started successfully.");
        } else {
            System.err.println("Python process failed to start.");
            throw new RuntimeException("Failed to start Python process.");
        }

        // 向 Python 脚本发送参数
        String input = param1 + " " + param2 + " " + param3 + "\n";
        writer.write(input);
        writer.flush(); // 确保数据被发送到 Python
        System.out.println("Sent parameters to Python script: " + input.trim());

        // 读取 Python 脚本的输出
        String result = reader.readLine();
        if (result == null) {
            throw new RuntimeException("Failed to read output from Python script.");
        }

        return result.trim(); // 返回结果并去除多余的换行符
    }

    /**
     * 关闭 Python 脚本进程。
     */
    @PreDestroy
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (process != null) {
                process.destroy(); // 终止 Python 进程
            }
            System.out.println("Python script terminated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}