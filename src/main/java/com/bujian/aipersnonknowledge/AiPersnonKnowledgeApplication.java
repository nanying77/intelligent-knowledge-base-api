package com.bujian.aipersnonknowledge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author
 * @Description 启动类
 * @Version 1.0
 */
@Slf4j
@SpringBootApplication
@EnableAsync      // 启用异步支持，用于用户行为记录等功能
@EnableScheduling // 启用定时任务，用于批量同步浏览量等
public class AiPersnonKnowledgeApplication {
    public static void main(String[] args) throws UnknownHostException{
        ConfigurableApplicationContext application = SpringApplication.run(AiPersnonKnowledgeApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = "/api";
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}