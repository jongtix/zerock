package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.ReviewRequestDto;
import com.jongtix.zerock.dto.response.ReviewResponseDto;
import com.jongtix.zerock.service.moviereview.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews/{mno}/all")
    public ResponseEntity<List<ReviewResponseDto>> getList(@PathVariable("mno") Long mno) {
        log.info("mno: " + mno);

        List<ReviewResponseDto> reviewResponseDtoList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewResponseDtoList, HttpStatus.OK);
    }

    @PostMapping("/reviews/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewRequestDto requestDto) {
        log.info("requestDto: " + requestDto);

        Long reviewNum = reviewService.register(requestDto);

        return new ResponseEntity<>(reviewNum, HttpStatus.CREATED); //CREATED: 응답코드 201, 요청이 정상 처리 되었고 새로운 리소스가 생김, POST & PUT
    }

    @PutMapping("/reviews/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable("reviewnum") Long reviewnum, @RequestBody ReviewRequestDto requestDto) {
        log.info("requestDto: " + requestDto);

        reviewService.modify(requestDto);

        return new ResponseEntity<>(reviewnum, HttpStatus.CREATED); //CREATED: 응답코드 201, 요청이 정상 처리 되었고 새로운 리소스가 생김, POST & PUT
    }

    @DeleteMapping("/reviews/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable("reviewnum") Long reviewnum) {
        log.info("reviewnum: " + reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.NO_CONTENT);  //NO_CONTENT: 응답코드 204, 요청은 정상처리 되었으나 컨텐츠를 제공하지 않음, PUT & DELETE
    }

}
