package com.example.Decentralized.Document.Vault.controllers.valut;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/valut")
public class ValutController {



    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("files")List<MultipartFile> files){
        if (files.isEmpty() || (files.size() == 1 && files.get(0).isEmpty())) {
            return ResponseEntity.badRequest().body("No files selected.");
        }

        for(MultipartFile file : files){
            System.out.println(file.getOriginalFilename());
        }


        return ResponseEntity.ok("ok");
    }
}
