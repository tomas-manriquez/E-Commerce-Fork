package com.example.ecommerce.services;

import com.example.ecommerce.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenService {
    @Autowired
    OrdenRepository ordenRepository;


}
