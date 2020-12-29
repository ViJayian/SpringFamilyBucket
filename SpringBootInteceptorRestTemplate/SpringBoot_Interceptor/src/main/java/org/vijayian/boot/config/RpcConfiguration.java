package org.vijayian.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * rpc.
 *
 * @author ViJay
 * @date 2020-12-28
 */
@ConfigurationProperties(prefix = "rpc")
@Data
public class RpcConfiguration {
    private String url;

}
