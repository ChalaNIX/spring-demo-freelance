package ua.in.lsrv.freelance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.in.lsrv.freelance.dto.UserDto;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.enums.UserRole;
import ua.in.lsrv.freelance.repository.UserRepository;
import ua.in.lsrv.freelance.request.SignUpRequest;
import ua.in.lsrv.freelance.util.UserPrincipalUtil;

import java.security.Principal;

@Service
public class UserService {
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserPrincipalUtil userPrincipalUtil;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserPrincipalUtil userPrincipalUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userPrincipalUtil = userPrincipalUtil;
    }

    public User createUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getConfirmPassword()));
        user.getRoles().add(UserRole.ROLE_USER);

        LOGGER.info("Saving user {}", user.getUsername());
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Cannot save user to database: " + e.getMessage());
            throw new RuntimeException("User " + user.getUsername() + " is not created");
        }
    }

    public User updateUser(UserDto userDto, Principal principal) {
        User user = userPrincipalUtil.getUserByPrincipal(principal);
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());

        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return userPrincipalUtil.getUserByPrincipal(principal);
    }

    public User getUserById(long id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with ID " + id));
    }
}
