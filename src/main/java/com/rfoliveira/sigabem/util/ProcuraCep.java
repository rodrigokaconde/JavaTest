package com.rfoliveira.sigabem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfoliveira.sigabem.model.ViaCep;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class ProcuraCep {
    public ProcuraCep() {
    }

    public ViaCep procuraCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        try {

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.of(10, SECONDS))
                    .build();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send
                    (httpRequest, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            ViaCep viaCep = new ViaCep();
            viaCep = mapper.readValue(httpResponse.body(), ViaCep.class);

            return viaCep;


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}