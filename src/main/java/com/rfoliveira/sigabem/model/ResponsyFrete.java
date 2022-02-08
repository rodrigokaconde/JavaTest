package com.rfoliveira.sigabem.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponsyFrete {
    Integer id;
    String cepOrigem;
    String cepDestino;
    float valorTotalFrete;
    LocalDate dataPrevistaEntrega;
}
