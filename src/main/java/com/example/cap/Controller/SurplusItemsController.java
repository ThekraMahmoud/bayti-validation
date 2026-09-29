package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.SurplusItems;
import com.example.cap.Service.SurplusItemsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surplus")
@AllArgsConstructor
public class SurplusItemsController {

    private final SurplusItemsService surplusItemsService;


    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(surplusItemsService.getAll());
    }


    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody SurplusItems surplusItems, Errors errors) {

         surplusItemsService.add(surplusItems);
             return ResponseEntity.status(200).body(new ApiResponse("Surplus item added successfully"));

    }


    @PutMapping("/update/sellerId/{sellerId}/surplusItemsId/{surplusItemsId}")
    public ResponseEntity<?> update(@PathVariable Integer sellerId, @PathVariable Integer surplusItemsId,  @RequestBody SurplusItems updateSurplusItems) {
        surplusItemsService.update(sellerId, surplusItemsId, updateSurplusItems);

        return ResponseEntity.status(200).body(new ApiResponse("Surplus item updated successfully"));
    }


    @PutMapping("/close/sellerId/{userId}/surplusItemsId/{surplusItemsId}")
    public ResponseEntity<?> close(@PathVariable Integer userId, @PathVariable Integer surplusItemsId) {

         surplusItemsService.closed(userId, surplusItemsId);
            return ResponseEntity.status(200).body(new ApiResponse("Surplus item closed successfully"));

    }

    @PutMapping("/reopen/sellerId/{userId}/surplusItemsId/{surplusItemsId}")
    public ResponseEntity<?> reopen(@PathVariable Integer userId, @PathVariable Integer surplusItemsId) {

        surplusItemsService.reopen(userId, surplusItemsId);
              return ResponseEntity.status(200).body(new ApiResponse("Surplus item reopened successfully"));

    }



    @PutMapping("/transfer/userId/{userId}/surplusItemsId/{surplusItemsId}/materialsId/{materialsId}/quantity/{quantity}")
    public ResponseEntity<?> transfer(@PathVariable Integer userId, @PathVariable Integer surplusItemsId, @PathVariable Integer materialsId, @PathVariable Integer quantity) {

    surplusItemsService.transfer(userId, surplusItemsId, materialsId, quantity);
            return ResponseEntity.status(200).body(new ApiResponse("Surplus shared successfully"));
    }
}