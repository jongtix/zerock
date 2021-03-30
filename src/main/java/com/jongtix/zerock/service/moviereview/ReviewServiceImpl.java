package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.moviereview.Movie;
import com.jongtix.zerock.domain.moviereview.Review;
import com.jongtix.zerock.domain.moviereview.ReviewRepository;
import com.jongtix.zerock.dto.request.ReviewRequestDto;
import com.jongtix.zerock.dto.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewResponseDto> getListOfMovie(Long mno) {
        return reviewRepository.findByMovie(
                Movie.builder()
                        .mno(mno)
                        .build()
        )
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewRequestDto requestDto) {
        return reviewRepository.save(dtoToEntity(requestDto)).getReviewnum();
    }

    @Override
    @Transactional
    public void modify(ReviewRequestDto requestDto) {

        Long reviewNum = requestDto.getReviewNum();
        Review review = reviewRepository.findById(reviewNum).orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰가 없습니다. reviewNum: " + reviewNum));

        review.update(requestDto.getText(), requestDto.getGrade());

    }

    @Override
    @Transactional
    public void remove(Long reviewNum) {
        reviewRepository.deleteById(reviewNum);
    }
}
