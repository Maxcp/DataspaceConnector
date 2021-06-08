package de.fraunhofer.isst.dataspaceconnector.services.external;


import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ExternalRestService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExternalRestService.class);

    @Value("${partchain.pseudopush.url}")
    private String pseudoPushURL;
    @Value("${partchain.pseudopush.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Autowired
    public ExternalRestService() {
        this.restTemplate = new RestTemplate();
    }

    @SneakyThrows
    public void doPost(URI id) {
        if (pseudoPushURL == null || pseudoPushURL.isEmpty()){
           throw  new IllegalArgumentException("PseudoPush URL is mandatory and cannot be null or empty!");
        }
        LOGGER.info("Sending message with Resource URI to {}", pseudoPushURL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        HttpEntity<PseudoPushResource> request = new HttpEntity<>(new PseudoPushResource(id.toString()), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(pseudoPushURL, HttpMethod.POST, request, String.class);
            LOGGER.info("Response from call: {}", response.getBody());
        } catch (HttpServerErrorException e) {
            LOGGER.error("Error: Response from call: {}", e.getMessage());
        }
    }
}
