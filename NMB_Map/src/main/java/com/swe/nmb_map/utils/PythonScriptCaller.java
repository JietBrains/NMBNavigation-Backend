package com.swe.nmb_map.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe.nmb_map.entity.Images;
import com.swe.nmb_map.mapper.ImagesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

@Component
public class PythonScriptCaller {
    private static final String SEARCH_URL = "http://localhost:5432/";

    @Autowired
    private final ImagesMapper imagesMapper;

    public PythonScriptCaller(ImagesMapper imagesMapper) {
        this.imagesMapper = imagesMapper;
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
        System.out.println("param1 = " + param1 + ", param2 = " + param2 + ", param3 = " + param3);
        String url;
        if ("search".equals(param1)) {
            // 对参数进行URL编码
            String param2Encoded = URLEncoder.encode(param2, "UTF-8");
            String param3Encoded = URLEncoder.encode(param3, "UTF-8");
            url = SEARCH_URL + param1 + "?startPoint=" + param2Encoded + "&endPoint=" + param3Encoded;
        } else {
            String param2Encoded = URLEncoder.encode(param2, "UTF-8");
            String param3Encoded = URLEncoder.encode(param3, "UTF-8");
            url = SEARCH_URL + param1 + "?startPoint=" + param2Encoded + "&type=" + param3Encoded;
        }

        // 发送HTTP请求获取结果（使用Java 11+的HttpClient）
        String result;
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            // 检查HTTP状态码
            if (response.statusCode() != 200) {
                throw new RuntimeException("HTTP请求失败: 状态码 " + response.statusCode());
            }

            result = response.body();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("无效的URL格式", e);
        }

        System.out.println(result);
        // 转换 Python 输出为目标格式
        List<Map<String, Object>> data = convertToData(result.trim());

        // 构建最终响应
        return Result.ok(data); // 使用 Result.ok() 方法封装数据
//        return Result.ok(null);
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


}