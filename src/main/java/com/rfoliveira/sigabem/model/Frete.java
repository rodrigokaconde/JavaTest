package com.rfoliveira.sigabem.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Frete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    float peso;
    String cepOrigem;
    String cepDestino;
    String nomeDestinatario;
    float valorTotalFrete;
    LocalDate dataPrevistaEntrega;
    LocalDate dataConsulta;
}
