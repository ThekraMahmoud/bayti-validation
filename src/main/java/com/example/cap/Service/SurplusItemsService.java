package com.example.cap.Service;

import com.example.cap.Api.ApiException;
import com.example.cap.Model.*;
import com.example.cap.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SurplusItemsService {
    private final SurplusItemsRepository surplusItemsRepository;
    private final MaterialsRepository materialsRepository;
    private final ConstructionProjectRepository constructionProjectRepository;
    private final ConstructionPhasesRepository constructionPhasesRepository;
    private final OffersRepository offersRepository;
    private final UserRepository userRepository;


    public List<SurplusItems> getAll(){
        return surplusItemsRepository.findAll();
    }


    public void add(SurplusItems surplusItems){

        // لاتحقق من الموارد اول لاني ابغا اتحقق من حالة المرحله
        Materials materials=materialsRepository.findMaterialsById(surplusItems.getMaterialId());

        if(materials==null){
            throw new ApiException( "Material not found");
        }

        if (surplusItemsRepository.existsByMaterialIdAndSellerId(
                surplusItems.getMaterialId(),
                surplusItems.getSellerId())) {
            throw new ApiException(  "Surplus item already exists for this material");
        }

        // التحقق من المرحلة وحالتها
        ConstructionPhases constructionPhases=constructionPhasesRepository.findConstructionPhasesById(materials.getConstructionPhaseId());

        if (constructionPhases == null) {
            throw new ApiException(  "Construction phase not found");
        }

        // لا يمكن بيع الفائض قبل انتهاء المرحلة
        if(!constructionPhases.getStatus().equals("COMPLETED")){
            throw new ApiException( "Surplus can only be listed after the construction phase is completed");
        }

        if (materials.getSurplusQuantity() <= 0) {
            throw new ApiException(  "There is no surplus available");
        }



        // التحقق من أن الفائض تابع لمشروع موجود وأن البائع هو مالك المشروع
      Offers offers=offersRepository.findOffersById(constructionPhases.getOffersId());
        if(offers==null){
            throw new ApiException(  "Offer not found");
        }

     ConstructionProject constructionProject=constructionProjectRepository.findConstructionProjectById(offers.getProjectId());
        if (constructionProject == null) {
            throw new ApiException(  "Construction project not found");
        }

        User user=userRepository.findUsersById(surplusItems.getSellerId());
        if (user == null) {
            throw new ApiException(  "User not found");
        }

        if (!constructionProject.getUserId().equals(surplusItems.getSellerId())) {
            throw new ApiException("You do not own this project");
        }


        surplusItems.setStatus("AVAILABLE");
        surplusItems.setQuantity(materials.getSurplusQuantity());
        surplusItems.setMaterialType(materials.getMaterialType());
        surplusItemsRepository.save(surplusItems);
    }


    public void update(Integer sellerId,Integer surplusItemsId,SurplusItems updatesurplusItems){

        SurplusItems surplusItems=surplusItemsRepository.findSurplusItemsByIdAndSellerId(surplusItemsId,sellerId);

        if(surplusItems==null){
            throw new ApiException("surplus Items NOT found ");
        }
        surplusItems.setDescription(updatesurplusItems.getDescription());
        surplusItems.setPrice(updatesurplusItems.getPrice());
        surplusItemsRepository.save(surplusItems);
    }



    public void closed(Integer userId, Integer surplusItemsId) {


        SurplusItems surplusItems = surplusItemsRepository.findSurplusItemsByIdAndSellerId(surplusItemsId, userId);
        if (surplusItems == null) {
            throw new ApiException("Surplus item not found");
        }
        if (surplusItems.getStatus().equals("SOLD")) {
            throw new ApiException( "Sold surplus item cannot be closed");
        }
        if (surplusItems.getStatus().equals("CLOSED")) {
            throw new ApiException( "Surplus item is already closed");
        }

        surplusItems.setStatus("CLOSED");
        surplusItemsRepository.save(surplusItems);
    }


    public void reopen(Integer userId, Integer surplusItemsId) {

        SurplusItems surplusItems = surplusItemsRepository.findSurplusItemsByIdAndSellerId(surplusItemsId, userId);

        if (surplusItems == null) {
            throw new ApiException( "Surplus item not found");
        }

        if (surplusItems.getStatus().equals("SOLD")) {
            throw new ApiException( "Sold surplus item cannot be reopened");
        }

        if (surplusItems.getStatus().equals("AVAILABLE")) {
            throw new ApiException( "Surplus item is already available");
        }

        surplusItems.setStatus("AVAILABLE");
        surplusItemsRepository.save(surplusItems);
    }




    public void transfer(Integer userId,Integer surplusItemsId,Integer materialsId,Integer quantity) {
        SurplusItems surplusItems = surplusItemsRepository.findSurplusItemsByIdAndSellerId( surplusItemsId,userId);
        if (surplusItems == null) {
            throw new ApiException("Surplus item not found");
        }
        if (surplusItems.getStatus().equals("SOLD")) {
            throw new ApiException("Surplus item is not available");
        }

        // التأكد أن نوع المادة متطابق
        Materials materials = materialsRepository.findMaterialsById(materialsId);
        if (materials == null) {
            throw new ApiException("Material not found");
        }

        ConstructionPhases constructionPhases=constructionPhasesRepository.findConstructionPhasesById(materials.getConstructionPhaseId());
            if(constructionPhases==null){
                throw new ApiException("Construction phase not found");
            }

//عشان ما يقدر يرسل المواد الا لحسابه فقط
            Offers offers =offersRepository.findOffersById(constructionPhases.getOffersId());
            if(offers==null){
                throw new ApiException("Offer not found");
            }

            ConstructionProject constructionProject=constructionProjectRepository.findConstructionProjectById(offers.getProjectId());

            if(constructionProject==null){
                throw new ApiException("Construction project not found");
            }
            if (!constructionProject.getUserId().equals(userId)) {
                throw new ApiException("You do not own this material");
            }


        if(!materials.getMaterialType().equals(surplusItems.getMaterialType())){
            throw new ApiException("Material type must be the same");
        }
        if (quantity <= 0) {
            throw new ApiException("Quantity must be greater than zero");
        }
        if(surplusItems.getQuantity()<quantity){
            throw new ApiException("Quantity exceeds available surplus");
        }

        materials.setPurchasedQuantity(materials.getPurchasedQuantity()+quantity);
        materials.setSurplusQuantity(materials.getPurchasedQuantity()-materials.getUsedQuantity());
        surplusItems.setQuantity(surplusItems.getQuantity()-quantity);
        if (surplusItems.getQuantity() == 0) {
            surplusItems.setStatus("SOLD");
        }
        materialsRepository.save(materials);
        surplusItemsRepository.save(surplusItems);

    }
}
