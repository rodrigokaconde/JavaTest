package com.rfoliveira.sigabem.util;

import com.rfoliveira.sigabem.model.ViaCep;

import java.time.LocalDate;

public class CalcularDataEntrega {
    public CalcularDataEntrega() {
    }

    public LocalDate calcularDataEntega(ViaCep viaCepOrigem, ViaCep viaCepDestino) {
        LocalDate dataPrevistaEntega = LocalDate.now();

        if (viaCepOrigem.getDdd().equals(viaCepDestino.getDdd()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(1);
        else if (viaCepOrigem.getUf().equals(viaCepDestino.getUf()))
            dataPrevistaEntega = dataPrevistaEntega.plusDays(3);
        else
            dataPrevistaEntega = dataPrevistaEntega.plusDays(10);

        return dataPrevistaEntega;
    }
}