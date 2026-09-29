package com.example.cap.Service;

import com.example.cap.Api.ApiException;
import com.example.cap.Model.*;
import com.example.cap.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ConstructionPhasesService {
     private final ConstructionPhasesRepository constructionPhasesRepository;
     private final OffersRepository offersRepository;

 private final EmailService emailService;
 private final UserRepository userRepository;
 private final ContractorsRepository contractorsRepository;
 private final ConstructionProjectRepository constructionProjectRepository;




    //اضافة مراحل البناء فقط
    public void add(ConstructionPhases phases){

        Offers offers=offersRepository.findOffersById(phases.getOffersId());
        if(offers==null){
            throw new ApiException("Offer not found");
        }

        if (!offers.getStatus().equals("ACCEPTED")) {
            throw new ApiException( "Offer is not accepted");
        }

        if(constructionPhasesRepository.existsByOffersIdAndPhaseName(phases.getOffersId(),phases.getPhaseName())){
            throw new ApiException( "Phase name already exists");
        }
        phases.setStatus("NOT DONE");
        phases.setStartDate(null);
        phases.setActualEndDate(null);

        constructionPhasesRepository.save(phases);
    }



    public void updatePhase(Integer contractorsId, Integer offerId, Integer phaseId, ConstructionPhases updatePhase) {

        Offers offers = offersRepository.findOffersById(offerId);

        if (offers == null) {
            throw new ApiException( "Offer not found");
        }

        if (!offers.getContractorId().equals(contractorsId)) {
            throw new ApiException( "You do not own this offer");
        }

        ConstructionPhases phases = constructionPhasesRepository.findConstructionPhasesById(phaseId);
        if (phases == null) {
            throw new ApiException( "Phase not found");
        }

        if (!phases.getOffersId().equals(offerId)) {
            throw new ApiException( "This phase does not belong to this offer");
        }

        if (phases.getStatus().equals("COMPLETED")) {
            throw new ApiException( "Completed phase cannot be updated");
        }
        phases.setPhaseName(updatePhase.getPhaseName());
        phases.setExpectedEndDate(updatePhase.getExpectedEndDate());

        constructionPhasesRepository.save(phases);
    }




    public void startPhase(Integer contractorsId, Integer offerId, Integer phaseId) {

        Offers offers = offersRepository.findOffersById(offerId);

        if (offers == null) {
            throw new ApiException( "Offer not found");
        }

        if (!offers.getContractorId().equals(contractorsId)) {
            throw new ApiException(  "You do not own this offer");
        }

        ConstructionPhases phases = constructionPhasesRepository.findConstructionPhasesById(phaseId);

        if (phases == null) {
            throw new ApiException(  "Phase not found");
        }

        if (!phases.getOffersId().equals(offerId)) {
            throw new ApiException(  "This phase does not belong to this offer");
        }

        if (!phases.getStatus().equals("NOT DONE")) {
            throw new ApiException(  "Phase has already started or completed");
        }

        phases.setStatus("IN PROGRESS");
        phases.setStartDate(LocalDate.now());

        constructionPhasesRepository.save(phases);

        ConstructionProject project = constructionProjectRepository.findConstructionProjectById(offers.getProjectId());


        User user = userRepository.findUsersById(project.getUserId());
        emailService.sendEmail(
                user.getEmail(),
                "Your Construction Phase Has Started - BAYTI",
                   "Hello " + user.getName() + ",\n\n" +
                        "We’re pleased to inform you that the contractor has officially started the " +
                        phases.getPhaseName() + " phase of your construction project.\n\n" +
                        "Phase: " + phases.getPhaseName() + "\n" +
                        "Start Date: " + phases.getStartDate() + "\n\n" +
                        "Your project is now in progress, and you can follow its progress through BAYTI.\n\n" +
                        "We’ll notify you once this phase has been completed.\n\n" +
                        "Thank you for using BAYTI."
        );
    }





    public void completePhase(Integer contractorsId, Integer offerId, Integer phaseId) {

 //لازم اتحقق من المتريال هنا انها موجوده قبل ما يسوي كوبليت
        Offers offers = offersRepository.findOffersById(offerId);
        if (offers == null) {
            throw new ApiException( "Offer not found");
        }
        if (!offers.getContractorId().equals(contractorsId)) {
            throw new ApiException( "You do not own this offer");
        }


        ConstructionPhases phases = constructionPhasesRepository.findConstructionPhasesById(phaseId);
        if (phases == null) {
            throw new ApiException( "Phase not found");
        }
        if (!phases.getOffersId().equals(offerId)) {
            throw new ApiException( "This phase does not belong to this offer");
        }
        if (!phases.getStatus().equals("IN PROGRESS")) {
            throw new ApiException( "Phase is not in progress");
        }

        phases.setStatus("COMPLETED");
        phases.setActualEndDate(LocalDate.now());

        constructionPhasesRepository.save(phases);



        //رسائل النجاح
        ConstructionProject project = constructionProjectRepository.findConstructionProjectById(offers.getProjectId());
        User user = userRepository.findUsersById(project.getUserId());

        emailService.sendEmail(
                user.getEmail(),
                "Construction Phase Completed - BAYTI",
                "Hello " + user.getName() + ",\n\n" +
                        "Good news! The contractor has successfully completed the " +
                        phases.getPhaseName() + " phase of your construction project.\n\n" +
                        "Phase: " + phases.getPhaseName() + "\n" +
                        "Start Date: " + phases.getStartDate() + "\n" +
                        "Completion Date: " + phases.getActualEndDate() + "\n\n" +
                        "You can review the materials used and the remaining quantities through your BAYTI project.\n\n" +
                        "Thank you for using BAYTI."
        );


        Contractors contractor = contractorsRepository.findContractorsById(offers.getContractorId());
        emailService.sendEmail(
                contractor.getEmail(),
                "Phase Completed Successfully - BAYTI",
                "Hello " + contractor.getName() + ",\n\n" +
                        "You have successfully completed the " +
                        phases.getPhaseName() + " phase of the construction project.\n\n" +
                        "Phase: " + phases.getPhaseName() + "\n" +
                        "Start Date: " + phases.getStartDate() + "\n" +
                        "Completion Date: " + phases.getActualEndDate() + "\n\n" +
                        "The project owner has been notified that the phase has been completed.\n\n" +
                        "Thank you for using BAYTI."
        );

    }











    }

