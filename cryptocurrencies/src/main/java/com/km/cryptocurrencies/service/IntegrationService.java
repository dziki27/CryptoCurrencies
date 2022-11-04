package com.km.cryptocurrencies.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.km.cryptocurrencies.configuration.StringDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    @Value("${coingecko.host.url}")
    private String hostUrl;

    private final ObjectMapper objectMapper;

    public <T> List<T> getListFromService(Class<T> clazz, String api) throws IOException, InterruptedException, URISyntaxException {
        HttpResponse<String> response = sendRequest(api);
        return objectMapper.readValue(response.body(), TypeFactory.defaultInstance().constructParametricType(ArrayList.class, clazz));
    }

    public Map<String, Object> getPricesToHashMap(String api) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = sendRequest(api);
        return objectMapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private HttpResponse<String> sendRequest(String api) throws URISyntaxException, IOException, InterruptedException {
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(module());

        HttpRequest request = HttpRequest.newBuilder().uri(new URI(hostUrl + api)).GET().build();
        HttpClient httpClient = HttpClient.newHttpClient();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    Module module() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StringDeserializer());
        return module;
    }
}
