package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.Lands;
import com.example.cap.Service.LandsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lands")
@AllArgsConstructor
public class LandsController {

    private final LandsService landsService;

//هنا فقط الي له صلاحية يشوف الارض المستخدم صاحب الارض وليس كل المستخدمين
    @GetMapping("/user/Landes/{id}")
    public ResponseEntity<?> getLandes(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(landsService.getLandes(id));
    }


    @PostMapping("/user/Landes/add")
    public ResponseEntity<?> add(@RequestBody @Valid Lands lands) {

        landsService.add(lands);
           return ResponseEntity.status(201).body(new ApiResponse("Land added successfully"));

        }


    @PutMapping("/user/Landes/update/user/{userId}/land/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @PathVariable Integer userId,  @RequestBody Lands landsUpdate) {


        landsService.update(userId, id, landsUpdate);
         return ResponseEntity.status(200).body(new ApiResponse("Land updated successfully"));


    }


    @DeleteMapping("/user/Landes/delete/user/{userId}/land/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @PathVariable Integer userId) {

        landsService.delete(userId, id);

            return ResponseEntity.status(200).body(new ApiResponse("Land deleted successfully"));

    }
}