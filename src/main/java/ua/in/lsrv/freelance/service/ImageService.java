package ua.in.lsrv.freelance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ua.in.lsrv.freelance.entity.Image;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.repository.ImageRepository;
import ua.in.lsrv.freelance.repository.UserRepository;
import ua.in.lsrv.freelance.util.ImageCompressionUtil;
import ua.in.lsrv.freelance.util.UserPrincipalUtil;

import java.io.IOException;
import java.security.Principal;

@Service
public class ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class.getName());

    private ImageRepository imageRepository;
    private UserPrincipalUtil userPrincipalUtil;
    private UserRepository userRepository;
    
    @Autowired
    public ImageService(ImageRepository imageRepository, UserPrincipalUtil userPrincipalUtil, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userPrincipalUtil = userPrincipalUtil;
        this.userRepository = userRepository;
    }

    public Image uploadUserImage(MultipartFile image, Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);
        LOGGER.info("Uploading profile image for user {}", user.getUsername());
        Image userImage = imageRepository.findByUserId(user.getId())
                .orElse(null);

        if (!ObjectUtils.isEmpty(userImage)) {
            imageRepository.delete(userImage);
        }
        Image imageForUpload = new Image();
        try {
            imageForUpload.setUserId(user.getId());
            imageForUpload.setImage(ImageCompressionUtil.compressImage(image.getBytes()));
            imageForUpload.setName(image.getOriginalFilename());
            return imageRepository.save(imageForUpload);

        } catch (IOException e) {
            LOGGER.error("Cannot save user profile image: " + e.getMessage());
        }
        return null;
    }

    public Image getUserImage(long userId) {
        Image image = imageRepository.findByUserId(userId)
                .orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            image.setImage(ImageCompressionUtil.decompressImage(image.getImage()));
        }

        return image;
    }
}
