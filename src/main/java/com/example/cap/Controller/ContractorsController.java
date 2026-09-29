package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.Contractors;
import com.example.cap.Service.ContractorsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contractor")
@AllArgsConstructor
public class ContractorsController {

    private final ContractorsService contractorsService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(contractorsService.gatContractors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Contractors contractors) {

       contractorsService.addContractors(contractors);
       return ResponseEntity.status(200).body(new ApiResponse("Verification code sent to your email"));

    }

    @PutMapping("/verifyEmail/{email}/{code}")
    public ResponseEntity<?> verifyEmail(@PathVariable String email, @PathVariable String code) {

        contractorsService.verifyEmail(email, code);
        return ResponseEntity.status(200).body(new ApiResponse("Email verified successfully .Your account is pending admin approval"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody  Contractors contractors) {

        contractorsService.update(id, contractors);

        return ResponseEntity.status(200).body(new ApiResponse("Contractor updated successfully"));

    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        contractorsService.delete(id);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor deleted successfully"));

    }
}

