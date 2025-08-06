package com.example.demo.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        String[] cacheNames = {"tasks", "pendingTasks", "task"};
        
        for (String cacheName : cacheNames) {
            CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
            if (cache != null) {
                CacheStats cacheStats = cache.getNativeCache().stats();
                Map<String, Object> cacheInfo = new HashMap<>();
                cacheInfo.put("hitCount", cacheStats.hitCount());
                cacheInfo.put("missCount", cacheStats.missCount());
                cacheInfo.put("hitRate", cacheStats.hitRate());
                cacheInfo.put("loadCount", cacheStats.loadCount());
                cacheInfo.put("evictionCount", cacheStats.evictionCount());
                cacheInfo.put("averageLoadPenalty", cacheStats.averageLoadPenalty());
                stats.put(cacheName, cacheInfo);
            }
        }
        
        return stats;
    }
} 