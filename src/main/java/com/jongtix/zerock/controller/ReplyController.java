package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.ReplyRequestDto;
import com.jongtix.zerock.dto.response.ReplyResponseDto;
import com.jongtix.zerock.service.board.ReplyService;
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
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/replies/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyResponseDto>> getListByBoard(@PathVariable("bno") Long bno) {
        log.info("bno: " + bno);

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("/replies")
    public ResponseEntity<Long> register(@RequestBody ReplyRequestDto requestDto) { //@RequestBody: JSON으로 들어오는 데이터를 자동으로 해당 타입의 객체로 매핑해 주는 역할
        log.info(requestDto);

        Long rno = replyService.register(requestDto);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/replies/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
        log.info("rno: " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/replies/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyRequestDto requestDto) {
        log.info(requestDto);

        replyService.modify(requestDto);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
