package ru.practicum.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.dto.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {
    @Autowired
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> userIds,
                                  Integer from,
                                  Integer size) {
        log.info("admin UserService getUsers ids {}, from {}, size {}", userIds, from, size);
        if (userIds == null || userIds.isEmpty()) {
            return UserMapper.toUserDto(userRepository.findAll(PageRequest.of(from / size, size,
                    Sort.by(Sort.Direction.ASC, "id"))));
        } else {
            return UserMapper.toUserDto(userRepository.findAllUsersByIds(userIds, PageRequest.of(from / size, size,
                    Sort.by(Sort.Direction.ASC, "id"))));
        }
    }


    public UserDto postUser(NewUserRequest newUserRequest) {
        log.info("admin UserService postUser newUserRequest {}", newUserRequest);
        try {
            if (!userRepository.findAllByName(newUserRequest.getName()).isEmpty())
                throw new ValidatedException("name is not new");
            User user = UserMapper.toUser(newUserRequest);
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public void deleteUser(Long userId) {
        log.info("admin UserController deleteUser userId {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            userRepository.delete(user);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}