package com.example.Cache_Server;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
@ConditionalOnWebApplication
public class ProxyService {

    @Value("${origin.url}")
    private String url;
    private RestClient restClient;
    private Cache cache;
    private static final String CACHE_HIT ="X-Cache";

    Logger log = LoggerFactory.getLogger(ProxyService.class);

    public ProxyService(RestClient restClient, CacheManager cacheManager){
        this.restClient = restClient;
        this.cache = cacheManager.getCache("Proxy");

    }

    public void clear(){
        log.info("Clearing Cache");
        cache.clear();
    }

    ResponseEntity<String> RequestCheck(HttpServletRequest request){
        CacheKey key= CacheKey.create(request);
        ResponseWrapper valueWrapper = cache.get(key, ResponseWrapper.class);
        if(valueWrapper != null){
            log.info("Response Coming from cache",key.url());
            return ResponseEntity
                    .status(valueWrapper.responseEntity().getStatusCode())
                    .headers(valueWrapper.responseEntity().getHeaders())
                    .header(CACHE_HIT, "HIT")
                    .body(valueWrapper.responseEntity().getBody());
        }
        else {
            ResponseEntity<String> response = restClient
                    .method(key.method())
                    .uri(key.url())
                    .headers(headers -> headers.addAll(key.headers()))
                    .body(key.body())
                    .retrieve()
                    .toEntity(String.class);

            log.info("Response Coming from origin", key.url());
            cache.put(key, new ResponseWrapper(response));
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .header(CACHE_HIT, "MISS")
                    .body(response.getBody());
        }

    }


}
