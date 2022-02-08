package com.rfoliveira.sigabem.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfoliveira.sigabem.model.CityViaCep;
import com.rfoliveira.sigabem.model.Frete;
import com.rfoliveira.sigabem.repository.FreteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class FreteService {
    @Autowired
    FreteRepository freteRepository;

    public Frete registerFrete(String cepDestino,
                               String cepOrigem,
                               String nomeDestinatario,
                               float peso) {
        Frete frete = new Frete();

        CityViaCep cityOrigem = loadCity(cepOrigem);
        CityViaCep cityDestino = loadCity(cepDestino);

        frete.setCepDestino(cepDestino);
        frete.setCepOrigem(cepOrigem);
        frete.setNomeDestinatario(nomeDestinatario);
        frete.setPeso(peso);
        frete.setDataConsulta(LocalDate.now());
        frete.setDataPrevistaEntrega(calcularDataEntega(cityOrigem, cityDestino));
        frete.setValorTotalFrete(calcularValorFrete(cityOrigem, cityDestino, peso));

        return freteRepository.save(frete);

    }

    private float calcularValorFrete(CityViaCep cityOrigem, CityViaCep cityDestino, float peso) {
        float valorFrete = peso*1;
        if(cityOrigem.getDdd().equals(cityDestino.getDdd()))
            valorFrete -= valorFrete*0.5;
        else if(cityOrigem.getUf().equals(cityDestino.getUf()))
            valorFrete -= valorFrete*0.75;

        return valorFrete;
    }

    private LocalDate calcularDataEntega(CityViaCep cityOrigem, CityViaCep cityDestino) {
        LocalDate dataPrevistaEntega = LocalDate.now();

        if(cityOrigem.getDdd().equals(cityDestino.getDdd()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(1);
        else if(cityOrigem.getUf().equals(cityDestino.getUf()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(3);
        else
            dataPrevistaEntega = dataPrevistaEntega.plusDays(10);

        return dataPrevistaEntega;
    }


    private CityViaCep loadCity(String cep) {
        String url = "https://viacep.com.br/ws/"+cep+"/json/";

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
            CityViaCep cityViaCep = new CityViaCep();
            cityViaCep = mapper.readValue(httpResponse.body(), CityViaCep.class);

            return cityViaCep;


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
