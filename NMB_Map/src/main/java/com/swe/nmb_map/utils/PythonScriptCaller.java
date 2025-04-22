package com.swe.nmb_map.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.nmb_map.entity.Images;
import com.swe.nmb_map.mapper.ImagesMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

@Component
public class PythonScriptCaller {

    private final ImagesMapper imagesMapper;
    private Process process; // 保存 Python 进程
    private BufferedWriter writer; // 用于向 Python 发送数据
    private BufferedReader reader; // 用于读取 Python 输出

    public PythonScriptCaller(ImagesMapper imagesMapper) {
        this.imagesMapper = imagesMapper;
    }

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
     * @return 格式化后的 JSON 字符串
     * @throws Exception 如果通信失败或读取输出时出错
     */
    public Result<List<Map<String, Object>>> callPythonScript(String param1, String param2, String param3) throws Exception {

        // 检查 Python 进程是否启动成功
        if (!process.isAlive()) {
            throw new RuntimeException("Python process is not running.");
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

        // 转换 Python 输出为目标格式
        List<Map<String, Object>> data = convertToData(result.trim());

        // 构建最终响应
        return Result.ok(data); // 使用 Result.ok() 方法封装数据
    }

    /**
     * 将 Python 返回的字符串转换为目标 JSON 格式。
     *
     * @param pythonOutput Python 脚本的输出字符串
     * @return 格式化后的 JSON 字符串
     * @throws Exception 如果解析失败
     */
    private List<Map<String, Object>> convertToData(String pythonOutput) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // Step 1: 替换单引号为双引号
        String cleanedOutput = pythonOutput.replace("'", "\"");

        // Step 2: 将 (x, y) 替换为 [x, y]
        cleanedOutput = cleanedOutput.replaceAll("\\((\\d+), (\\d+)\\)", "[$1, $2]");

        // Step 3: 解析清洗后的 JSON 字符串
        List<Map<String, Object>> pythonResultList = objectMapper.readValue(cleanedOutput,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

        // Step 4: 构建目标 JSON 结构
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Map<String, Object> pythonResult : pythonResultList) {
            Map<String, Object> navigation = new LinkedHashMap<>();
            // get url
            List<Object> building = List.of(pythonResult.get("building"));
            List<String> url = new ArrayList<>();
            for (Object o : building) {
                String index = o.toString() + ".png";
                QueryWrapper<Images> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("image", index);
                Images image = imagesMapper.selectOne(queryWrapper);
                url.add(image.getUrl());
            }
            // photo
            navigation.put("photo", url);

            // coordinate
            List<List<Map<String, Integer>>> coordinates = new ArrayList<>();
            List<Map<String, Integer>> innerCoordinates = new ArrayList<>();
            List<?> path = (List<?>) pythonResult.get("path");
            for (Object point : path) {
                List<?> coords = (List<?>) point;
                Map<String, Integer> coordinate = new HashMap<>();
                coordinate.put("x", (Integer) coords.get(0));
                coordinate.put("y", (Integer) coords.get(1));
                innerCoordinates.add(coordinate);
            }
            coordinates.add(innerCoordinates); // 外层数组只有一个元素
            navigation.put("coordinate", coordinates);

            // 构建 data 元素
            Map<String, Object> dataElement = new HashMap<>();
            dataElement.put("navigation", navigation);
            dataList.add(dataElement);
        }

        return dataList;
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