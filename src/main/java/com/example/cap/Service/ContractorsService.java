package com.example.cap.Service;

import com.example.cap.Api.ApiException;
import com.example.cap.Model.Contractors;
import com.example.cap.Model.Pending.EmailVerification;
import com.example.cap.Model.Pending.PendingContractor;
import com.example.cap.Repository.ContractorsRepository;
import com.example.cap.Repository.Pending.EmailVerificationRepository;
import com.example.cap.Repository.Pending.PendingContractorRepository;
import com.example.cap.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class ContractorsService {

    private final ContractorsRepository contractorsRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;
    private final PendingContractorRepository pendingContractorRepository;
    private final UserRepository userRepository;

    // Get all contractors
    public List<Contractors> gatContractors() {
        return contractorsRepository.findAll();
    }


    // Add contractor
    public void addContractors(Contractors contractors) {

        PendingContractor pendingContractor = pendingContractorRepository.findByEmail(contractors.getEmail());

        if (contractorsRepository.findByEmail(contractors.getEmail()) != null||userRepository.findByEmail(contractors.getEmail())!=null||pendingContractor!=null&&pendingContractor.isEmailVerified()) {
            throw new ApiException( "Email already used");
        }

        if (contractorsRepository.findByPhone(contractors.getPhone()).isPresent()) {
            throw new ApiException("Phone already used");
        }

        if (contractorsRepository.findByLicenseNumber(contractors.getLicenseNumber()).isPresent()) {
             throw new ApiException("License Number already used");
        }


        // Generate verification code
        String code = String.valueOf(1000 + new Random().nextInt(9000));


        // Create or update pending contractor
        if (pendingContractor == null) {
            pendingContractor = new PendingContractor();
        }

        pendingContractor.setName(contractors.getName());
        pendingContractor.setEmail(contractors.getEmail());
        pendingContractor.setPhone(contractors.getPhone());
        pendingContractor.setCompanyName(contractors.getCompanyName());
        pendingContractor.setBio(contractors.getBio());
        pendingContractor.setExperience(contractors.getExperience());
        pendingContractor.setLicenseNumber(contractors.getLicenseNumber());
        pendingContractor.setLocation(contractors.getLocation());
        pendingContractor.setPassword(contractors.getPassword());
        pendingContractor.setUrlPortfolio(contractors.getUrlPortfolio());

        pendingContractorRepository.save(pendingContractor);


        // Create or update verification code
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(contractors.getEmail());

        if (emailVerification == null) {
            emailVerification = new EmailVerification();
        }

        emailVerification.setEmail(contractors.getEmail());
        emailVerification.setCode(code);
        emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        emailVerificationRepository.save(emailVerification);


        // Send verification email
        emailService.sendEmail(
                contractors.getEmail(),
                "BAYTI - Email Verification Code",
                "Hello " + contractors.getName() + ",\n\n" +
                        "Thank you for registering with BAYTI.\n\n" +
                        "To complete your account registration, please use the verification code below:\n\n" +
                        "Verification Code: " + code + "\n\n" +
                        "This code is valid for 10 minutes. For your security, please do not share this code with anyone.\n\n" +
                        "If you did not request to create a BAYTI account, please ignore this email.\n\n" +
                        "Thank you for choosing BAYTI.\n\n" +
                        "Best regards,\n" +
                        "BAYTI Team"
        );
    }


    // Verify email
    public void verifyEmail(String email, String code) {

        EmailVerification emailVerification =
                emailVerificationRepository.findByEmailAndCode(email, code);

        if (emailVerification == null) {
            throw new ApiException( "Invalid email or verification code");
        }

        if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiException("Verification code expired");
        }

        PendingContractor pendingContractor =
                pendingContractorRepository.findByEmail(email);

        if (pendingContractor == null) {
            throw new ApiException("Registration data not found");
        }
        pendingContractor.setEmailVerified(true);
        pendingContractorRepository.save(pendingContractor);
        emailVerificationRepository.delete(emailVerification);

    }


    // Update contractor
    public void update(Integer id, Contractors contractors) {

        Contractors up = contractorsRepository.findContractorsById(id);

        if (up == null) {
            throw new ApiException( "Contractor not found");
        }

        if (!up.getIsVerified()) {
            throw new ApiException( "Account is not active");
        }

        up.setBio(contractors.getBio());
        up.setCompanyName(contractors.getCompanyName());
        up.setName(contractors.getName());
        up.setLocation(contractors.getLocation());
        up.setUrlPortfolio(contractors.getUrlPortfolio());
        up.setExperience(contractors.getExperience());

        contractorsRepository.save(up);

    }


    // Delete contractor
    public void delete(Integer id) {
        Contractors toDelete =
                contractorsRepository.findContractorsById(id);

        if (toDelete == null) {
            throw new ApiException( "Contractor not found");
        }

        if (!toDelete.getIsVerified()) {
            throw new ApiException( "Account is not active");
        }

        contractorsRepository.delete(toDelete);

        emailService.sendEmail(
                toDelete.getEmail(),
                "Account Deleted - BAYTI",
                "Hello " + toDelete.getName() + ",\n\n" +
                        "Your BAYTI account has been successfully deleted.\n\n" +
                        "We are sorry to see you leave, and we would appreciate knowing if there was any issue that led you to delete your account. " +
                        "Your feedback helps us improve the BAYTI experience for everyone.\n\n" +
                        "If you deleted your account by mistake or would like to restore it, please contact our technical support team, " +
                        "and we will assist you with the account recovery process.\n\n" +
                        "Thank you for being part of BAYTI.\n\n" +
                        "Best regards,\n" +
                        "BAYTI Team"
        );
    }
}