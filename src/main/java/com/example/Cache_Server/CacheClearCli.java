package com.example.Cache_Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnNotWebApplication
public class CacheClearCli {

    //We need to send a request to the server to clear the cache
    //so we cant implement application runner directly but we need to create a bean so that we can still read the cli args
    //shouldn't use service class but rather send a request to the server

    @Value("${target.port}")
    private String port;

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:" + port)
                .build();
    }

    Logger log = LoggerFactory.getLogger(CacheClearCli.class);

    @Bean
    ApplicationRunner delete(RestClient restClient) {
        return args -> {
            for (String arg : args.getSourceArgs()) {
                if ("--clear-cache".equals(arg)) {
                    log.info("Cache Clear Mode Enabled");


                }}
            log.info("Cache Clearin in progresss...");
            // If clearCacheMode is true, proceed to clear the cache
            try {
                restClient
                        .delete()
                        .uri("/clear")
                        .retrieve()
                        .toBodilessEntity();
                log.info("Cache Cleared");
            } catch (Exception e) {
                log.error("Failed to clear cache: {}", e.getMessage());
            }
        };

    }
}
