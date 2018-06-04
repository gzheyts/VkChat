package ru.ssnd.demo.vkchat.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"ru.ssnd.demo.vkchat.service"})
@PropertySource("classpath:chat_service.properties")
public class SpringRootConfig {

}