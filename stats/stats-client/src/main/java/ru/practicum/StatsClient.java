package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient {
    private static final String API_GET = "/stats";

    private static final String API_POST = "/hit";

    private static final String statsUrl = "http://stats-server:9090";

    private final RestTemplate rest;

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();

    }

    public ResponseEntity<Object> getStats(String start,
                                           String end,
                                           List<String> uris,
                                           Boolean unique
    ) {
        log.info("getStats");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique == true ? true : false);
        StringBuilder url = new StringBuilder(API_GET + "?start={start}&end={end}&unique={unique}");
        for (int i = 0; i < uris.size(); i++) {
            log.info("i {}", i);
            url.append("&uris={uris" + i + "}");
            parameters.put("uris" + i, uris.get(i));
        }
        log.info("STATS-CLIENT" + url + "parameters {}", parameters);

        return get(url.toString(), parameters);
    }

    public ResponseEntity<Object> setStats(StatsDto statsDto) {
        log.info("STATS-CLIENT put");
        return post(API_POST, null, statsDto);
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        log.info("STATSCLIENT post path {}, body {}", path, body);
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> serverResponse;
        log.info("makeAndSendRequest 1 path {}, requestEntity {}, body {}", path, requestEntity, body);
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            log.info("makeAndSendRequest error {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        log.info("makeAndSendRequest body {}", body);
        return prepareResponse(serverResponse);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        log.info("prepareResponse response {}", response);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
