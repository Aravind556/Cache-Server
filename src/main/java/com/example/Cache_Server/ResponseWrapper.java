package com.example.Cache_Server;

import org.springframework.http.ResponseEntity;

public record ResponseWrapper(ResponseEntity<String> responseEntity) {
}
