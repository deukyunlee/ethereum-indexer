package com.deukyunlee.indexer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Controller
public class FaviconController {

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}