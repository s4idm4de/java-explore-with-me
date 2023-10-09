package ru.practicum.admin.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
public class AdminUserController {
    @Autowired
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> userIds,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("admin user controller getUsers ids {}, from {}, size {}", userIds, from, size);
        return adminUserService.getUsers(userIds, from, size);
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody @Validated NewUserRequest newUserRequest) {

        log.info("admin UserController postUser newUserRequest {}", newUserRequest);
        return new ResponseEntity<>(adminUserService.postUser(newUserRequest), HttpStatus.CREATED);

    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("admin UserController deleteUser userId {}", userId);
        adminUserService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
