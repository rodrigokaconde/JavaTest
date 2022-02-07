package com.rfoliveira.sigabem.service;

import com.rfoliveira.sigabem.repository.FreteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreteService {
    @Autowired
    FreteRepository freteRepository;

}
