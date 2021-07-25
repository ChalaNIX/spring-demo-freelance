package ua.in.lsrv.freelance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.in.lsrv.freelance.dto.UserDto;
import ua.in.lsrv.freelance.entity.User;
import ua.in.lsrv.freelance.facade.UserFacade;
import ua.in.lsrv.freelance.service.UserService;
import ua.in.lsrv.freelance.validation.ResponseErrorValidator;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidator responseErrorValidator;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidator responseErrorValidator) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidator = responseErrorValidator;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDto userDto = userFacade.userToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        User user = userService.updateUser(userDto, principal);
        UserDto updatedUserDto = userFacade.userToDto(user);
        return ResponseEntity.ok(updatedUserDto);
    }
}
