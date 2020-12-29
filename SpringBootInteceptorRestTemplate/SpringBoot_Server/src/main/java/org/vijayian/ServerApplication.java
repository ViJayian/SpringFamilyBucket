package org.vijayian;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.Serializable;

/**
 * ServerApplication.
 *
 * @author ViJay
 * @date 2020-12-28
 */
@SpringBootApplication
@Slf4j
public class ServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        final String portVal = "server.port";
        String port = environment.getProperty(portVal);
        log.info("server run at [ http://localhost:{} ]", port);
    }
}
