package com.rfoliveira.sigabem.model;

import lombok.Data;

@Data
public class ViaCep {
    String cep;
    String logradouro;
    String complemento;
    String bairro;
    String localidade;
    String uf;
    String ibge;
    String gia;
    String ddd;
    String siafi;
}
