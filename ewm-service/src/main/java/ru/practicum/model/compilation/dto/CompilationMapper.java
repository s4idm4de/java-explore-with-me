package ru.practicum.model.compilation.dto;

import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.dto.EventMapper;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .events(compilation.getEvents() == null ? new ArrayList<>() : EventMapper.toEventShortDto(compilation.getEvents()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toCompilationDto(Iterable<Compilation> compilations) {
        List<CompilationDto> result = new ArrayList<>();

        for (Compilation compilation : compilations) {
            result.add(toCompilationDto(compilation));
        }

        return result;
    }

    public static Compilation toCompilation(UpdateCompilationRequest updateCompilationRequest, Compilation compilation) {
        return Compilation.builder()
                .title(updateCompilationRequest.getTitle() == null ? compilation.getTitle() : updateCompilationRequest.getTitle())
                .pinned(updateCompilationRequest.getPinned() == null ? compilation.getPinned() : updateCompilationRequest.getPinned())
                .id(compilation.getId())
                .build();
    }
}
