package com.rfoliveira.sigabem.util;

import com.rfoliveira.sigabem.model.ViaCep;

public class CalcularFrete {
    public CalcularFrete() {
    }

    public float calcularValorFrete(ViaCep cityOrigem, ViaCep cityDestino, float peso) {
        float valorFrete = peso * 1;
        if (cityOrigem.getDdd().equals(cityDestino.getDdd()))
            valorFrete -= valorFrete * 0.5;
        else if (cityOrigem.getUf().equals(cityDestino.getUf()))
            valorFrete -= valorFrete * 0.75;

        return valorFrete;
    }
}