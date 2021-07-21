package ua.in.lsrv.freelance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.in.lsrv.freelance.entity.Image;
import ua.in.lsrv.freelance.payload.MessageResponse;
import ua.in.lsrv.freelance.service.ImageService;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/image")
public class ImageController {
    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadUserImage(@RequestParam("file") MultipartFile file, Principal principal) {
        imageService.uploadUserImage(file, principal);

        return ResponseEntity.ok(new MessageResponse("User image was uploaded"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Image> getUserImage(@PathVariable String userId) {
        Image image = imageService.getUserImage(Long.parseLong(userId));
        return ResponseEntity.ok(image);
    }
}
