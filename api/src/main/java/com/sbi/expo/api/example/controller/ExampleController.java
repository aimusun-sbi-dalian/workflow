package com.sbi.expo.api.example.controller;

import com.sbi.expo.api.base.ResponseBase;
import com.sbi.expo.api.example.dto.ExampleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/example")
public class ExampleController {

    @GetMapping
    public ResponseBase<ExampleDTO> example() {
        return ResponseBase.ok(new ExampleDTO());
    }
}
