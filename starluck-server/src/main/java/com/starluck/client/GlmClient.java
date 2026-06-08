package com.starluck.client;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智谱 GLM-4-Flash AI 客户端
 * 用于运营后台的 AI 回复建议功能
 *
 * @author AI
 * @date 2026-06-08
 * @ai-assisted ai辅助生成，开发人员已完成审查与测试。
 */
@Component
public class GlmClient {

    @Value("${glm.api-url}")
    private String apiUrl;

    @Value("${glm.api-key:}")
    private String apiKey;

    @Value("${glm.model:glm-4-flash}")
    private String model;

    @Value("${glm.max-tokens:300}")
    private int maxTokens;

    @Value("${glm.temperature:0.85}")
    private double temperature;

    private static final int TIMEOUT_MS = 30000;

    public String chat(List<Map<String, String>> messages) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("GLM API Key 未配置，请在 application.yml 中设置 glm.api-key");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", temperature);
        body.put("max_tokens", maxTokens);

        String respStr;
        try {
            respStr = cn.hutool.http.HttpRequest.post(apiUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(TIMEOUT_MS)
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException("GLM API 请求失败: " + e.getMessage(), e);
        }

        JSONObject resp = JSONUtil.parseObj(respStr);
        if (resp.containsKey("error")) {
            String errMsg = resp.getByPath("error.message", String.class);
            throw new RuntimeException("GLM API 返回错误: " + (errMsg != null ? errMsg : respStr));
        }

        JSONArray choices = resp.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("GLM API 未返回有效内容");
        }

        return choices.getJSONObject(0)
                .getByPath("message.content", String.class);
    }
}
