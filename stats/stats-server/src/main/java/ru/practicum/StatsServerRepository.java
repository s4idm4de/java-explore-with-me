package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.StatsData;
import ru.practicum.model.StatsOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<StatsData, Long> {

    @Query("SELECT new ru.practicum.model.StatsOut(s.app, s.uri, COUNT(s.ip)) FROM StatsData s " +
            "WHERE s.uri in ?3 AND s.timestamp >= ?1 AND s.timestamp <= ?2 " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<StatsOut> findStatsWithParams(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.StatsOut(s.app, s.uri, COUNT(distinct s.ip)) FROM StatsData s " +
            "WHERE s.uri in ?3 AND s.timestamp >= ?1 AND s.timestamp <= ?2 " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(distinct s.ip) DESC")
    List<StatsOut> findStatsWithParamsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.StatsOut(s.app, s.uri, COUNT(s.ip)) FROM StatsData s " +
            "WHERE s.timestamp >= ?1 AND s.timestamp <= ?2 " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(s.ip) DESC")
    List<StatsOut> findStatsWithParamsAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.StatsOut(s.app, s.uri, COUNT(distinct s.ip)) FROM StatsData s " +
            "WHERE s.timestamp >= ?1 AND s.timestamp <= ?2 " +
            "GROUP BY s.uri, s.app ORDER BY COUNT(distinct s.ip) DESC")
    List<StatsOut> findStatsWithParamsUniqueAll(LocalDateTime start, LocalDateTime end);
}
