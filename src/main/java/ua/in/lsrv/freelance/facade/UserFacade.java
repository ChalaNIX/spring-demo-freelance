package ua.in.lsrv.freelance.facade;

import org.springframework.stereotype.Component;
import ua.in.lsrv.freelance.dto.UserDto;
import ua.in.lsrv.freelance.entity.User;

@Component
public class UserFacade {
    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setLastname(user.getLastname());

        return userDto;
    }
}
