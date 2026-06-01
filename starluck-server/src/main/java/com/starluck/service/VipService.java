package com.starluck.service;

import java.util.Map;

/**
 * VIP服务接口
 *
 * @author AI
 * @date 2026-06-01
 */
public interface VipService {

    Map<String, Object> getPlans();

    void subscribe(Long userId, String planType, String payMethod);
}
