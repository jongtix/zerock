package com.jongtix.zerock.service.user;

import com.jongtix.zerock.domain.user.ClubMember;
import com.jongtix.zerock.domain.user.Note;
import com.jongtix.zerock.dto.request.NoteRequestDto;
import com.jongtix.zerock.dto.response.NoteResponseDto;

import java.util.List;

public interface NoteService {

    Long register(NoteRequestDto noteRequestDto);

    NoteResponseDto get(Long num);

    void modify(NoteRequestDto noteRequestDto);

    void remove(Long num);

    List<NoteResponseDto> getAllWithWriter(String writerEmail);

    default Note dtoToEntity(NoteRequestDto noteRequestDto) {
        return Note.builder()
                .num(noteRequestDto.getNum())
                .title(noteRequestDto.getTitle())
                .content(noteRequestDto.getContent())
                .writer(
                        ClubMember.builder()
                                .email(noteRequestDto.getWriterEmail())
                                .build()
                )
                .build();
    }

    default NoteResponseDto entityToDto(Note note) {
        return NoteResponseDto.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();
    }

}
