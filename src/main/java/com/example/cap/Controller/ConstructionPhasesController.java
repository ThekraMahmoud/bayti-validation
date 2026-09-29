package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.ConstructionPhases;
import com.example.cap.Service.ConstructionPhasesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/phases")
@AllArgsConstructor
public class ConstructionPhasesController {

    private final ConstructionPhasesService constructionPhasesService;


    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ConstructionPhases phases, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

         constructionPhasesService.add(phases);
         return ResponseEntity.status(200).body(new ApiResponse("Phase added successfully"));

    }



    @PutMapping("/update/contractorsId/{contractorsId}/offerId/{offerId}/phaseId/{phaseId}")
    public ResponseEntity<?> update(@PathVariable Integer contractorsId, @PathVariable Integer offerId, @PathVariable Integer phaseId, @RequestBody ConstructionPhases updatePhase) {

         constructionPhasesService.updatePhase(contractorsId, offerId, phaseId, updatePhase);

         return ResponseEntity.status(200).body(new ApiResponse("Phase updated successfully"));


    }
    


    @PutMapping("/start/contractorsId/{contractorsId}/offerId/{offerId}/phaseId/{phaseId}")
    public ResponseEntity<?> startPhase(@PathVariable Integer contractorsId, @PathVariable Integer offerId, @PathVariable Integer phaseId) {

        constructionPhasesService.startPhase(contractorsId, offerId, phaseId);
         return ResponseEntity.status(200).body(new ApiResponse("Phase started successfully"));

    }


    @PutMapping("/complete/contractorsId/{contractorsId}/offerId/{offerId}/phaseId/{phaseId}")
    public ResponseEntity<?> completePhase(@PathVariable Integer contractorsId, @PathVariable Integer offerId, @PathVariable Integer phaseId) {

       constructionPhasesService.completePhase(contractorsId, offerId, phaseId);

            return ResponseEntity.status(200).body(new ApiResponse("Phase completed successfully"));

    }
}