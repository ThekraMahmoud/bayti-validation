package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.User;
import com.example.cap.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?>get(){
       return ResponseEntity.status(200).body(userService.getUser());
    }




    @PostMapping("/add")
    public ResponseEntity<?>add(@RequestBody @Valid User user, Errors errors){
        if(errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("Verification code sent to your email"));
    }





    @PutMapping("/verifyEmail/{email}/{code}")
    public ResponseEntity<?>verifyEmail(@PathVariable String email ,@PathVariable String code){

        userService.verifyEmail(email,code);

         return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?>update(@PathVariable Integer id ,@RequestBody  User user){

            userService.update(id,user);
           return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id) {

        userService.delete(id);
        return ResponseEntity.status(200).body(new ApiResponse("delete successful"));

    }
}
