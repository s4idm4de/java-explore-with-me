package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class StatsServerController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final StatsServerServer statsServer;

    public StatsServerController(StatsServerServer statsServer) {
        this.statsServer = statsServer;
    }

    @GetMapping("/stats")
    public List<StatsDtoOut> getStats(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique
    ) {

        LocalDateTime startDttm = LocalDateTime.parse(start, formatter);
        LocalDateTime endDttm = LocalDateTime.parse(end, formatter);
        log.info("STATS-SERVER CONTROLLER getStats start {}, end {}, uris {}, unique {}", startDttm, endDttm, uris, unique);
        return statsServer.getStats(startDttm, endDttm, uris, unique);
    }

    @PostMapping("/hit")
    public void setStats(@RequestBody StatsDto statsDto) {
        log.info("STATS-CONTROLLER setStats {}", statsDto);
        statsServer.setStats(statsDto);
    }
}
