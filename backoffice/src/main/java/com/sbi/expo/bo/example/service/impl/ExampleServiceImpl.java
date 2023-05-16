package com.sbi.expo.bo.example.service.impl;

import com.sbi.expo.bo.example.service.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class ExampleServiceImpl implements ExampleService {}
