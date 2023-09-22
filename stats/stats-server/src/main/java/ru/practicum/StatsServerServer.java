package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StatsServerServer {

    @Autowired
    private final StatsServerRepository statsRepository;

    public List<StatsDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("STATS SERVER getStats start {}, end {}, uris {}, unique {}", start, end, uris, unique);
        if (unique && uris != null) return StatsMapper.toStatsDtoOut(statsRepository.findStatsWithParamsUnique(start, end, uris));
        if (unique && uris == null)
            return StatsMapper.toStatsDtoOut(statsRepository.findStatsWithParamsUniqueAll(start, end));
        if (uris == null) return StatsMapper.toStatsDtoOut(statsRepository.findStatsWithParamsAll(start, end));
        return StatsMapper.toStatsDtoOut(statsRepository.findStatsWithParams(start, end, uris));
    }

    public void setStats(StatsDto statsDto) {
        log.info("STATS-SERVER setStats {}", statsDto);
        statsRepository.save(StatsMapper.toStatsData(statsDto));
    }
}
