package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PutMapping("/approve/{email}")
    public ResponseEntity<?> approveContractor(@PathVariable String email) {

        adminService.approveContractor(email);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor approved successfully"));
    }


    @DeleteMapping("/reject/{email}")
    public ResponseEntity<?> rejectContractor(@PathVariable String email) {

        adminService.rejectContractor(email);
        return ResponseEntity.status(200).body("Contractor rejected successfully");
    }



    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {

        return ResponseEntity.status(200).body(adminService.getUser());
    }


    @GetMapping("/contractors")
    public ResponseEntity<?> getContractors() {
        return ResponseEntity.status(200).body(adminService.getContractors());
    }

    @GetMapping("/contractors/pending")
    public ResponseEntity<?> getPendingContractors() {
        return ResponseEntity.status(200).body(adminService.getPendingContractors());
    }


    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {

       adminService.deleteUser(email);
            return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));


    }


    @PutMapping("/add-admin/{email}")
    public ResponseEntity<?> addAdmin(@PathVariable String email) {

         adminService.addAdmin(email);
            return ResponseEntity.status(200).body(new ApiResponse("User promoted to admin successfully"));

    }


    @DeleteMapping("/contractor/{email}")
    public ResponseEntity<?> deleteContractor(@PathVariable String email) {

        adminService.deleteContractors(email);
        return ResponseEntity.status(200).body(new ApiResponse("Contractor deleted successfully"));
    }
}
