package com.automate.df.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class RestTemplateUtil {

    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    public RestTemplateUtil(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public <T> HttpEntity<?> exchange(String url, Map<String, String> headersMap, Map<String, String> queryParams,
                                      T request, Class<?> response, HttpMethod method,
                                      HttpServletRequest httpServletRequest) {

        if (headersMap == null) {
            headersMap = new HashMap<>();
        }
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }

        HttpHeaders headers = new HttpHeaders();

        for (Entry<String, String> entry : headersMap.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        for (Entry<String, String> entry : queryParams.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        HttpEntity<?> entity = null;

        if (request != null && HttpMethod.POST.equals(method))
            entity = new HttpEntity<>(request, headers);

        if (request == null && HttpMethod.GET.equals(method))
            entity = new HttpEntity<>(headers);

        Utils.ObjectToJson(request);

        HttpEntity<?> entityResponse = null;
        if (entity != null)
            entityResponse = restTemplate.exchange(builder.build().encode().toUri(), method, entity, response);
        Utils.ObjectToJson(entityResponse);
        return entityResponse;
    }

    @SuppressWarnings("unchecked")
    public <T, S> S postForObject(String url, Map<String, String> headersMap, Map<String, String> queryParams,
                                  T request, Class<?> response, HttpServletRequest httpServletRequest) {
        HttpEntity<?> exchange = exchange(url, headersMap, queryParams, request, response, HttpMethod.POST, httpServletRequest);
        return (S) exchange.getBody();
    }

}
