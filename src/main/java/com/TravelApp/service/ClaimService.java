package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.TravelApp.entity.Claim;
import com.TravelApp.entity.ClaimDetails;
import com.TravelApp.entity.Post;
import com.TravelApp.entity.User;
import com.TravelApp.repository.ClaimRepository;
import com.TravelApp.repository.PostRepository;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private PostService postService;

    private static final String NEW_CLAIM_STATUS = "NEW";
    private static final String APPROVED_CLAIM_STATUS = "APRVD";
    private static final String REJECTED_CLAIM_STATUS = "RJCTD";
    private static final String FILE_URL = "uploads/claim-details/";

    public Claim claimPost(User user, String message, Integer postId, MultipartFile[] files){       
        Post post = postRepository.findById(postId).get();
        LocalDateTime currTime = LocalDateTime.now();

        Claim claim = new Claim();
        claim.setUser(user);
        claim.setPost(post);
        claim.setStatus(NEW_CLAIM_STATUS);
        claim.setCreatedDate(currTime);
        claim.setDescription(message);

        List<ClaimDetails> claimDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            ClaimDetails claimDetail = new ClaimDetails();
            claimDetail.setOriginalFileName(file.getOriginalFilename());
            claimDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            claimDetail.setFileName(UUID.randomUUID().toString() + "." + claimDetail.getFileType());
            claimDetail.setClaim(claim);
            claimDetail.setCreatedDate(currTime);
            claimDetail.setUrl(FILE_URL);
            fileService.save(file, claimDetail);
            claimDetails.add(claimDetail);
        });

        claim.setClaimDetails(claimDetails);

        //auto rejection
        if(claim.getPost().getUser().equals(user)){
            claim.setStatus(REJECTED_CLAIM_STATUS);
            claim.setComments("You cannot claim your Own Post!");
        }

        return claimRepository.save(claim);
    }

    public Page<Claim> getAllClaims(Pageable pageable, String status){
        if(status.equalsIgnoreCase("ALL")){
            return claimRepository.findAll(pageable);
        }
        return claimRepository.findByStatus(pageable, status);
    }

    public Claim approveClaim(Integer claimId){
        Claim claim = claimRepository.findById(claimId).get();
        claim.setStatus(APPROVED_CLAIM_STATUS);
        claim.setComments("Your Claim has been Approved!");

        //Set Claimed Post to NEW User
        postService.claimPost(claim.getUser(), claim.getPost());
        return claimRepository.save(claim);
    }

    public Claim rejectClaim(Integer claimId, String message){
        Claim claim = claimRepository.findById(claimId).get();
        claim.setStatus(REJECTED_CLAIM_STATUS);
        claim.setComments(message);
        return claimRepository.save(claim);
    }

    
}
