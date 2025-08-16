package com.example.Cache_Server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
@ConditionalOnWebApplication
public class ProxyController {
    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @DeleteMapping("/clear")
    public void delete() {
        proxyService.clear();
    }

    @RequestMapping("/**")
    public ResponseEntity<String> Request(HttpServletRequest request) {
        return proxyService.RequestCheck(request);
    }
}
