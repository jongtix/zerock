package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.NoteRequestDto;
import com.jongtix.zerock.dto.response.NoteResponseDto;
import com.jongtix.zerock.service.user.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping(value = "/notes/")
    public ResponseEntity<Long> register(@RequestBody NoteRequestDto dto) {
        log.info("----------------register----------------");
        log.info(dto);

        Long num = noteService.register(dto);

        return new ResponseEntity<>(num, HttpStatus.CREATED);
    }

    @GetMapping(value = "/notes/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteResponseDto> read(@PathVariable("num") Long num) {
        log.info("----------------read----------------");
        log.info(num);

        NoteResponseDto responseDto = noteService.get(num);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/notes/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NoteResponseDto>> getList(String email) {
        log.info("----------------getList----------------");
        log.info(email);

        List<NoteResponseDto> noteList = noteService.getAllWithWriter(email);

        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/notes/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> remove(@PathVariable("num") Long num) {
        log.info("----------------remove----------------");
        log.info(num);

        noteService.remove(num);

        return new ResponseEntity<>("removed", HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/notes/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modify(@RequestBody NoteRequestDto dto) {
        log.info("----------------modify----------------");
        log.info(dto);

        noteService.modify(dto);

        return new ResponseEntity<>("modified", HttpStatus.CREATED);
    }

}
