package ua.in.lsrv.freelance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.repository.UserRepository;;

import java.security.Principal;

@Component
public class UserPrincipalUtil {
    private final UserRepository userRepository;

    @Autowired
    public UserPrincipalUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));
    }
}
