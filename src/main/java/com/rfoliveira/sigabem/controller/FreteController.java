package com.rfoliveira.sigabem.controller;

import com.rfoliveira.sigabem.service.FreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frete")
public class FreteController {
    @Autowired
    FreteService freteService;
}
