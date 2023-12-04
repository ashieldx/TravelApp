package com.TravelApp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String FILE_URL = "uploads/claim-details/";
    private static final String NEW_CLAIM_STATUS = "NEW";

    public Claim claimPost(User user, Integer postId, MultipartFile[] files){
        Post post = postRepository.findById(postId).get();
        LocalDateTime currentTime = LocalDateTime.now();

        Claim claim = new Claim();
        claim.setUser(user);
        claim.setPost(post);
        claim.setStatus(NEW_CLAIM_STATUS);

        List<ClaimDetails> claimDetails = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file-> {
            ClaimDetails claimDetail = new ClaimDetails();
            claimDetail.setOriginalFileName(file.getOriginalFilename());
            claimDetail.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1));
            claimDetail.setFileName(UUID.randomUUID().toString() + "." + claimDetail.getFileType());
            claimDetail.setClaim(claim);
            claimDetail.setCreatedDate(currentTime);
            claimDetail.setUrl(FILE_URL);
            fileService.save(file, claimDetail);
            claimDetails.add(claimDetail);
        });

        claim.setClaimDetails(claimDetails);
        return claimRepository.save(claim);
    }
    
}
