package ru.practicum.privateCategory.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
public class RequestController {
    @Autowired
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable Long userId) {
        log.info("private RequestController getRequestsOfUser userId {}", userId);
        return requestService.getrequestsOfUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto postRequest(@PathVariable Long userId, @RequestParam(name="eventId") Long eventId) {
        log.info("private RequestController postRequest userId {}, eventId {}", userId, eventId);
        return requestService.postRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchRequest(@PathVariable Long userId, @RequestParam(name="requestId") Long requestId) {
        log.info("private RequestController patchRequest userId {}, requestId {}", userId, requestId);
        return requestService.patchRequest(userId, requestId);
    }
}
