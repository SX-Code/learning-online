package com.swx.orders.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.swx.orders.mapper")
public class MybatisPlusConfig {
}
