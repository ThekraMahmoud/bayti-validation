package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.ConstructionProject;
import com.example.cap.Service.ConstructionProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
@AllArgsConstructor
public class ConstructionProjectController {

    private final ConstructionProjectService constructionProjectService;


    // كل مشاريع البناء تظهر للمستخدمين
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(constructionProjectService.getAll());
    }


    // فقط مشاريع المستخدم
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> get(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(constructionProjectService.get(userId));
    }


    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ConstructionProject project) {
        constructionProjectService.add(project);
            return ResponseEntity.status(200).body(new ApiResponse("add successfully"));

    }


    @PutMapping("/update/userId/{userId}/projectId/{projectId}")
    public ResponseEntity<?> update(@PathVariable Integer userId, @PathVariable Integer projectId,  @RequestBody ConstructionProject updateProject) {

        constructionProjectService.update(userId, projectId, updateProject);
             return ResponseEntity.status(200).body(new ApiResponse("Construction project updated successfully"));
    }


    @DeleteMapping("/delete/userId/{userId}/projectId/{projectId}")
    public ResponseEntity<?> delete(@PathVariable Integer userId, @PathVariable Integer projectId) {

       constructionProjectService.delete(userId, projectId);
            return ResponseEntity.status(200).body(new ApiResponse("Construction project Delete successfully" ));
    }
}