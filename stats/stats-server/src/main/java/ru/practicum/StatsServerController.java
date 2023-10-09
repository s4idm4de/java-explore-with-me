package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class StatsServerController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final StatsServerService statsServer;

    public StatsServerController(StatsServerService statsServer) {
        this.statsServer = statsServer;
    }

    @GetMapping("/stats")
    public List<StatsDtoOut> getStats(@RequestParam(name = "start") String start,
                                      @RequestParam(name = "end") String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique
    ) {
        try {
            if (((start == null || start.isBlank()) && end != null) || (start != null && (end == null || end.isBlank())))
                throw new ApiException("wrong dates");
            LocalDateTime startDttm = LocalDateTime.parse(start, formatter);
            LocalDateTime endDttm = LocalDateTime.parse(end, formatter);
            log.info("STATS-SERVER CONTROLLER getStats start {}, end {}, uris {}, unique {}", startDttm, endDttm, uris, unique);
            if (startDttm.isAfter(endDttm)) throw new ApiException("wrong dates");
            return statsServer.getStats(startDttm, endDttm, uris, unique);
        } catch (ApiException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> setStats(@RequestBody StatsDto statsDto) {
        log.info("STATS-CONTROLLER setStats {}", statsDto);
        statsServer.setStats(statsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
