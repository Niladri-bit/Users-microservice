package com.assignment.user.service.UserService.Services;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

//
//import com.assignment.order.service.OrderService.DTO.BookDTO;
//import com.assignment.order.service.OrderService.DTO.StockUpdateRequestDTO;
//import com.assignment.order.service.OrderService.exceptions.BookNotFoundException;

@Service
public class VaccinationCommunicationService {

    @Autowired
    private RestTemplate restTemplate;

  
    public void deleteVaccinationsForAStudent(Long id) {
        
        String url = "http://localhost:8082/vaccinations/students/" + id;

        ResponseEntity<String> vaccinationResponse = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null, // No body
                String.class
        );
        
    }


}

