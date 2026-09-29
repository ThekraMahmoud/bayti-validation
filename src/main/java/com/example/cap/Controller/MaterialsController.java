package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.Materials;
import com.example.cap.Service.MaterialsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/materials")
@AllArgsConstructor
public class MaterialsController {

    private final MaterialsService materialsService;


    @GetMapping("/get/phaseId/{phaseId}")
    public ResponseEntity<?> get(@PathVariable Integer phaseId) {

        return ResponseEntity.status(200)
                .body(materialsService.get(phaseId));
    }


    @PostMapping("/add/contractorsId/{contractorsId}")
    public ResponseEntity<?> add(@PathVariable Integer contractorsId, @Valid @RequestBody Materials materials) {

         materialsService.add(contractorsId, materials);
         return ResponseEntity.status(200).body(new ApiResponse("Material added successfully"));

    }


    @PutMapping ("/purchase/contractorsId/{contractorsId}/materialsId/{materialsId}/count/{count}")

    public ResponseEntity<?> increasePurchasedQuantity(@PathVariable Integer contractorsId, @PathVariable Integer materialsId, @PathVariable Integer count) {
        increasePurchasedQuantity(contractorsId, materialsId, count);

            return ResponseEntity.status(200).body(new ApiResponse("Material quantity increased successfully"));

    }

    @PutMapping("/use/contractorsId/{contractorsId}/materialsId/{materialsId}/count/{count}")
    public ResponseEntity<?> increaseUsedQuantity(@PathVariable Integer contractorsId, @PathVariable Integer materialsId, @PathVariable Integer count) {
           materialsService.increaseUsedQuantity(contractorsId, materialsId, count);
            return ResponseEntity.status(200).body(new ApiResponse("Material quantity used successfully"));
    }
}