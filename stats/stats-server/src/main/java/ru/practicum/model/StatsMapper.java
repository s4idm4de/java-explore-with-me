package ru.practicum.model;

import ru.practicum.StatsDto;
import ru.practicum.StatsDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static StatsData toStatsData(StatsDto statsDto) {
        return StatsData.builder()
                .uri(statsDto.getUri())
                .ip(statsDto.getIp())
                .app(statsDto.getApp())
                .timestamp(LocalDateTime.parse(statsDto.getTimestamp(), formatter))
                .build();
    }

    public static StatsDto toStatsDto(StatsData statsData) {
        return StatsDto.builder()
                .uri(statsData.getUri())
                .ip(statsData.getIp())
                .app(statsData.getApp())
                .timestamp(statsData.getTimestamp().format(formatter))
                .build();
    }

    public static List<StatsDto> toStatsDto(List<StatsData> statsDatas) {
        return statsDatas.stream().map(statsData -> toStatsDto(statsData)).collect(Collectors.toList());
    }

    public static StatsDtoOut toStatsDtoOut(StatsOut statsOut) {
        return StatsDtoOut.builder()
                .hits(statsOut.getHits())
                .app(statsOut.getApp())
                .uri(statsOut.getUri())
                .build();
    }

    public static List<StatsDtoOut> toStatsDtoOut(List<StatsOut> statsOuts) {
        return statsOuts.stream().map(statsOut -> toStatsDtoOut(statsOut)).collect(Collectors.toList());
    }
}
