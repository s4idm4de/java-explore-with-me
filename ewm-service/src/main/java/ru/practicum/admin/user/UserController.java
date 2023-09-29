package ru.practicum.admin.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.LengthValidation;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;

import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name="ids", required = false) List<Long> userIds,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("admin user controller getUsers ids {}, from {}, size {}", userIds, from, size);
        return userService.getUsers(userIds, from, size);
    }

    @PostMapping
    public UserDto postUser(@RequestBody @Validated NewUserRequest newUserRequest) {

            log.info("admin UserController postUser newUserRequest {}", newUserRequest);
            return userService.postUser(newUserRequest);

    }

    @DeleteMapping(path="/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("admin UserController deleteUser userId {}", userId);
        userService.deleteUser(userId);
    }
}
