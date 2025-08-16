package com.example.Cache_Server;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableCaching
@ConditionalOnWebApplication
public class ProxyConfig {

    @Bean
    ProxyService proxyservice(RestClient restClient,CacheManager cacheManager){
        return new ProxyService(restClient,cacheManager);
    }



    @Bean
    RestClient restClient(RestClient.Builder builder,@Value("${origin.url}") String url) {
        return builder
                .baseUrl(url)
                .build();
    }
}
