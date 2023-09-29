package ru.practicum.privateCategory.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        log.info("private RequestService getRequestsOfUser userId {}", userId);

    }


    public ParticipationRequestDto postRequest(Long userId, Long eventId) {
        log.info("private RequestService postRequest userId {}, eventId {}", userId, eventId);

    }


    public ParticipationRequestDto patchRequest(Long userId, Long requestId) {
        log.info("private RequestService patchRequest userId {}, requestId {}", userId, requestId);

    }
}
