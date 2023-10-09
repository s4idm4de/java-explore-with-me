package ru.practicum.privateCategory.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
public class PrivateRequestController {
    @Autowired
    private final PrivateRequestService privateRequestService;

    public PrivateRequestController(PrivateRequestService privateRequestService) {
        this.privateRequestService = privateRequestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable Long userId) {
        log.info("private RequestController getRequestsOfUser userId {}", userId);
        return privateRequestService.getRequestsOfUser(userId);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> postRequest(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        log.info("private RequestController postRequest userId {}, eventId {}", userId, eventId);
        return new ResponseEntity<>(privateRequestService.postRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("private RequestController patchRequest userId {}, requestId {}", userId, requestId);
        return privateRequestService.patchRequest(userId, requestId);
    }
}
