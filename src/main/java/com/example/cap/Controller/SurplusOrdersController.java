package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.SurplusOrders;
import com.example.cap.Service.SurplusOrdersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surplus/orders")
@AllArgsConstructor
public class SurplusOrdersController {

    private final SurplusOrdersService surplusOrdersService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody SurplusOrders surplusOrders) {

         surplusOrdersService.add(surplusOrders);
         return ResponseEntity.status(200).body(new ApiResponse("Surplus order created successfully"));


    }


    @PutMapping("/pay/buyerId/{buyerId}/surplusOrderId/{surplusOrderId}")
    public ResponseEntity<?> pay(@PathVariable Integer buyerId, @PathVariable Integer surplusOrderId) {

         surplusOrdersService.pay(buyerId, surplusOrderId);
            return ResponseEntity.status(200).body(new ApiResponse("Surplus order paid successfully"));

    }
}