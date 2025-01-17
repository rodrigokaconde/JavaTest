package com.rfoliveira.sigabem.controller;

import com.rfoliveira.sigabem.model.Frete;
import com.rfoliveira.sigabem.model.ResponsyFrete;
import com.rfoliveira.sigabem.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/frete")
public class FreteController {
    @Autowired
    FreteService freteService;

    @PostMapping
    public Frete registerFrete (@RequestParam String cepOrigem,
                                @RequestParam String cepDestino,
                                @RequestParam String nomeDestinatario,
                                @RequestParam float peso){

        return freteService.registerFrete(cepDestino, cepOrigem, nomeDestinatario, peso);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsyFrete> getFreteById(@PathVariable Integer id){
        return freteService.getFreteById(id);
    }

}
