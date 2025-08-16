package com.example.Cache_Server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public record CacheKey(
        HttpMethod method,
        String body,
        String url,
        HttpHeaders headers
) {

    private static final List<String> FILTERED_HEADERS = List.of("host", "accept-encoding");

    public static CacheKey create(HttpServletRequest request) {

        return new CacheKey(
                HttpMethod.valueOf(request.getMethod()),
                requestbody(request),
                request.getRequestURI(),
                extractHeaders(request)
        );

    }

    private static String requestbody(HttpServletRequest request){
        try{
            return request.getReader().lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
    }
    private static HttpHeaders extractHeaders(HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // need to filter out certain headers as they create different keys for the same request
            // and cause cache misses
            // for example, "Host" and "Accept-Encoding" headers are not relevant for caching
            // and can lead to cache misses if they are included in the key
            // so we filter them out
            if (!FILTERED_HEADERS.contains(headerName.toLowerCase(Locale.ROOT))){
                headers.add(headerName, request.getHeader(headerName));}

        }
        return headers;

    }

}
