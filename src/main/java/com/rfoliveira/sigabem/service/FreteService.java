package com.rfoliveira.sigabem.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfoliveira.sigabem.model.ResponsyFrete;
import com.rfoliveira.sigabem.model.ViaCep;
import com.rfoliveira.sigabem.model.Frete;
import com.rfoliveira.sigabem.repository.FreteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

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

        ViaCep viaCepOrigem = procuraCep(cepOrigem);
        ViaCep viaCepDestino = procuraCep(cepDestino);

        frete.setCepDestino(cepDestino);
        frete.setCepOrigem(cepOrigem);
        frete.setNomeDestinatario(nomeDestinatario);
        frete.setPeso(peso);
        frete.setDataConsulta(LocalDate.now());
        frete.setDataPrevistaEntrega(calcularDataEntega(viaCepOrigem, viaCepDestino));
        frete.setValorTotalFrete(calcularValorFrete(viaCepOrigem, viaCepDestino, peso));

        return freteRepository.save(frete);

    }

    private float calcularValorFrete(ViaCep cityOrigem, ViaCep cityDestino, float peso) {
        float valorFrete = peso*1;
        if(cityOrigem.getDdd().equals(cityDestino.getDdd()))
            valorFrete -= valorFrete*0.5;
        else if(cityOrigem.getUf().equals(cityDestino.getUf()))
            valorFrete -= valorFrete*0.75;

        return valorFrete;
    }

    private LocalDate calcularDataEntega(ViaCep viaCepOrigem, ViaCep viaCepDestino) {
        LocalDate dataPrevistaEntega = LocalDate.now();

        if(viaCepOrigem.getDdd().equals(viaCepDestino.getDdd()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(1);
        else if(viaCepOrigem.getUf().equals(viaCepDestino.getUf()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(3);
        else
            dataPrevistaEntega = dataPrevistaEntega.plusDays(10);

        return dataPrevistaEntega;
    }


    private ViaCep procuraCep(String cep) {
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

    public ResponseEntity<ResponsyFrete> getFreteById(Integer id) {
        Optional<Frete> frete =  freteRepository.findById(id);
        ResponsyFrete responsyFrete = new ResponsyFrete();

        if(frete.isPresent()){
            responsyFrete.setId(frete.get().getId());
            responsyFrete.setValorTotalFrete(frete.get().getValorTotalFrete());
            responsyFrete.setCepDestino(frete.get().getCepDestino());
            responsyFrete.setCepOrigem(frete.get().getCepOrigem());
            responsyFrete.setDataPrevistaEntrega(frete.get().getDataPrevistaEntrega());
            return ResponseEntity.ok().body(responsyFrete);
        }
        return ResponseEntity.notFound().build();
    }
}
