package com.rfoliveira.sigabem.service;


import com.rfoliveira.sigabem.model.ResponsyFrete;
import com.rfoliveira.sigabem.model.ViaCep;
import com.rfoliveira.sigabem.model.Frete;
import com.rfoliveira.sigabem.repository.FreteRepository;
import com.rfoliveira.sigabem.util.CalcularDataEntrega;
import com.rfoliveira.sigabem.util.CalcularFrete;
import com.rfoliveira.sigabem.util.ProcuraCep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FreteService {
    private final CalcularFrete calcularFrete = new CalcularFrete();
    private final CalcularDataEntrega calcularDataEntrega = new CalcularDataEntrega();
    private final ProcuraCep procuraCep = new ProcuraCep();
    @Autowired
    FreteRepository freteRepository;

    public Frete registerFrete(String cepDestino,
                               String cepOrigem,
                               String nomeDestinatario,
                               float peso) {
        Frete frete = new Frete();

        ViaCep viaCepOrigem = procuraCep.procuraCep(cepOrigem);
        ViaCep viaCepDestino = procuraCep.procuraCep(cepDestino);

        frete.setCepDestino(cepDestino);
        frete.setCepOrigem(cepOrigem);
        frete.setNomeDestinatario(nomeDestinatario);
        frete.setPeso(peso);
        frete.setDataConsulta(LocalDate.now());
        frete.setDataPrevistaEntrega(calcularDataEntrega.calcularDataEntega(viaCepOrigem, viaCepDestino));
        frete.setValorTotalFrete(calcularFrete.calcularValorFrete(viaCepOrigem, viaCepDestino, peso));

        return freteRepository.save(frete);

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
