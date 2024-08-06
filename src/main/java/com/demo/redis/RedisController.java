package com.demo.redis;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Redis Controller", description = "Controller for Redis")
public class RedisController {
    private final RedisService service;

    @GetMapping("/find/{key}")
    @Operation(summary = "Say Hello", description = "Returns a simple greeting message")
    public ResponseEntity<String> findCache(@PathVariable String key) {
        if (service.existsInCache(key)) {
            return ResponseEntity.ok().body(service.getFromCache(key));
        }
        return ResponseEntity.ok("Not found");
    }

    @PutMapping("/put/{key}/{value}")
    public ResponseEntity<String> saveCache(@PathVariable String key, @PathVariable String value) {
        return ResponseEntity.ok().body(service.addToCache(key, value));
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Long> deleteCache(@PathVariable String key) {
        return ResponseEntity.ok().body(service.deleteFromCache(key));
    }

    @PutMapping("/putList")
    public ResponseEntity<String> saveList(){
        return ResponseEntity.ok(service.addListToCache());
    }
}
