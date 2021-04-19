package com.jongtix.zerock.service.user;

import com.jongtix.zerock.domain.user.Note;
import com.jongtix.zerock.domain.user.NoteRepository;
import com.jongtix.zerock.dto.request.NoteRequestDto;
import com.jongtix.zerock.dto.response.NoteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Long register(NoteRequestDto noteRequestDto) {

        Note note = dtoToEntity(noteRequestDto);

        log.info("====================");
        log.info(note);

//        noteRepository.save(note);

        return noteRepository.save(note).getNum();
    }

    //TO-BE
    @Override
    public NoteResponseDto get(Long num) {
        Note result = noteRepository.getWithWriter(num).orElseThrow(() -> new IllegalArgumentException("해당하는 노트가 없습니다. num: " + num));

        return entityToDto(result);
    }

//    //AS-IS
//    @Override
//    public NoteResponseDto get(Long num) {
//
//        Optional<Note> result = noteRepository.getWithWriter(num);
//
//        if(result.isPresent()) {
//            return entityToDto(result.get());
//        }
//
//        return null;
//    }

    @Transactional
    @Override
    public void modify(NoteRequestDto noteRequestDto) {

        Long num = noteRequestDto.getNum();

        Note result = noteRepository.findById(num).orElseThrow(() -> new IllegalArgumentException("해당하는 노트가 없습니다. num: " + num));
        result.changeTitle(noteRequestDto.getTitle());
        result.changeContent(noteRequestDto.getContent());

    }

    @Override
    public void remove(Long num) {

        noteRepository.deleteById(num);

    }

    @Override
    public List<NoteResponseDto> getAllWithWriter(String writerEmail) {

        List<Note> noteList = noteRepository.getList(writerEmail);

        return noteList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

}
