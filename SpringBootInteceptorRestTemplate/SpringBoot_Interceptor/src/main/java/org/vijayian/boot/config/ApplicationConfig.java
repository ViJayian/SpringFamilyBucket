package org.vijayian.boot.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 服务配置.
 *
 * @author ViJay
 * @date 2020-12-24
 */
@Configuration
@EnableConfigurationProperties({RpcConfiguration.class})
@Slf4j
public class ApplicationConfig {

    private final RpcConfiguration rpcConfiguration;

    private final RestTemplate restTemplate;

    public ApplicationConfig(RpcConfiguration rpcConfiguration, RestTemplate restTemplate) {
        this.rpcConfiguration = rpcConfiguration;
        this.restTemplate = restTemplate;
    }

    public class ParamInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, ServletException {
            requestInfo(request);

            String method = request.getMethod();
            HttpMethod httpMethod = HttpMethod.valueOf(method);
            if (httpMethod == HttpMethod.GET) {
                //pathvariable || requestparam
                getRequestHandle(request, response);
            } else if (httpMethod == HttpMethod.POST) {
                //requestparam || requestbody || file
                postRequsetHandle(request, response);
            } else {
                return true;
            }
            return false;
        }

        private void postRequsetHandle(HttpServletRequest request, HttpServletResponse response) {
        }

        private void getRequestHandle(HttpServletRequest request, HttpServletResponse response) {
            String method = request.getMethod();
            HttpMethod httpMethod = HttpMethod.valueOf(method);
            String uri = request.getRequestURI();
            Map<String, String[]> parameterMap = request.getParameterMap();
            String reqUri = null;
            if (parameterMap.size() == 0) {
                reqUri = uri;
            } else {
                Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
                Iterator<Map.Entry<String, String[]>> iterator = entries.iterator();
                StringBuilder paramBuilder = new StringBuilder("?");
            }
            ResponseEntity<Object> responseEntity =
                    restTemplate.exchange(rpcConfiguration.getUrl() + uri, httpMethod, null, Object.class);

        }

        private void requestInfo(HttpServletRequest request) throws IOException, ServletException {
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String method = request.getMethod();
            log.info("url = {},uri = {},method = {}", url, uri, method);

            String queryString = request.getQueryString();//url中的请求参数 id=12&name=hello
            ServletInputStream stream = request.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(stream);
            String streamString = new String(bytes, request.getCharacterEncoding());
            Map<String, String[]> parameterMap = request.getParameterMap();//url中参数，表单参数
            Enumeration<String> parameterNames = request.getParameterNames();
            log.info("queryString = {}", queryString);
            log.info("streamString = {}", streamString);
            log.info("parameterMap = {}", parameterMap);
            log.info("parameterNames = {}", parameterNames);
            //>> TODO the request doesn't contain a multipart/form-data or multipart/mixed stream, content type header is application/json
//            Collection<Part> parts = request.getParts();
//            log.info("parts = {}", parts);
        }

        /**
         * requestbody
         *
         * @param request
         */
        private void requestBodyHandle(HttpServletRequest request, HttpServletResponse response) {
            try {
                ServletInputStream inputStream = request.getInputStream();
                byte[] bytes = StreamUtils.copyToByteArray(inputStream);
                String requestBody = new String(bytes, request.getCharacterEncoding());
                System.out.println(requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void requestParamHandle(HttpServletRequest request, HttpServletResponse response) {
            String method = request.getMethod();
            HttpMethod httpMethod = HttpMethod.valueOf(method);
            ResponseEntity<Object> entity = restTemplate.exchange(rpcConfiguration.getUrl() + request.getRequestURI(), httpMethod, null, Object.class);
            printJson(entity, response);
        }

        private void pathVariableHandle(HttpServletRequest request, HttpServletResponse response) {
            String uri = request.getRequestURI();
            System.out.println(uri);// /helloPathVariable/12/hello
            ResponseEntity<Object> entity = restTemplate.exchange(rpcConfiguration.getUrl() + uri, HttpMethod.GET, null, Object.class);
            printJson(entity, response);
        }

        private void printJson(ResponseEntity<Object> entity, HttpServletResponse response) {
            Object jsonResponse = entity.getBody();
            String json = JSON.toJSONString(jsonResponse);
            try (PrintWriter writer = response.getWriter()) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                //>> TODO getWriter() has already been called for this response
                //>> TODO printwrite调用过了，controller还返回了结果导致，因此拦截器设置false不再调用controller
                writer.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Configuration
    public class InterceptorConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new ParamInterceptor()).addPathPatterns("/**");
        }
    }

}
