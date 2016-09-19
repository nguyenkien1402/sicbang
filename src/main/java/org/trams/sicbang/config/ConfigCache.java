package org.trams.sicbang.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by voncount on 5/16/16.
 */
@Configuration
@EnableCaching
public class ConfigCache {

    @Bean
    public CacheManager cacheManager() {

        List<Cache> caches = new ArrayList<>();
        caches.add(new ConcurrentMapCache("categoryByName"));
        caches.add(new ConcurrentMapCache("categories"));
        caches.add(new ConcurrentMapCache("cities"));

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
