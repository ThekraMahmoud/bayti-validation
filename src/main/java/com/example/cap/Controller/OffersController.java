package com.example.cap.Controller;

import com.example.cap.Api.ApiResponse;
import com.example.cap.Model.Offers;
import com.example.cap.Service.OffersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
@AllArgsConstructor
public class OffersController {

    private final OffersService offersService;


    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody Offers offers, Errors errors) {

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();return ResponseEntity.status(400).body(new ApiResponse(message));
        }
         offersService.add(offers);
        return ResponseEntity.status(200).body(new ApiResponse("Offer added successfully"));
    }



    @PutMapping("/update/contractorId/{contractorId}/offersId/{offersId}")
    public ResponseEntity<?> update(@PathVariable Integer contractorId, @PathVariable Integer offersId,  @RequestBody Offers updateOffers, Errors errors) {


        offersService.update(contractorId, offersId, updateOffers);
             return ResponseEntity.status(200).body(new ApiResponse("Offer update successfully"));
    }


    @DeleteMapping("/delete/contractorId/{contractorId}/offersId/{offersId}")
    public ResponseEntity<?> delete(@PathVariable Integer contractorId, @PathVariable Integer offersId) {
           offersService.delete(contractorId, offersId);
            return ResponseEntity.status(200).body(new ApiResponse("Offer cancelled successfully"));

    }


    @GetMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<?> getOffersForUser(@PathVariable Integer userId, @PathVariable Integer projectId) {

        List<Offers> offers = offersService.getOffersForUser(userId, projectId);
        if (offers == null) {return ResponseEntity.status(400).body(new ApiResponse("You do not own this project"));}
        return ResponseEntity.status(200).body(offers);
    }


    @PutMapping("/accept/userId/{userId}/offersId/{offersId}")
    public ResponseEntity<?> acceptOffers(@PathVariable Integer userId, @PathVariable Integer offersId) {

       offersService.acceptOffers(userId, offersId);
            return ResponseEntity.status(200).body(new ApiResponse("Offer accepted successfully" ));

    }

}