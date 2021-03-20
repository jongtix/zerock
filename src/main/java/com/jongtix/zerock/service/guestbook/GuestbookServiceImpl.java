package com.jongtix.zerock.service.guestbook;

import com.jongtix.zerock.domain.guestbook.Guestbook;
import com.jongtix.zerock.domain.guestbook.GuestbookRepository;
import com.jongtix.zerock.domain.guestbook.QGuestbook;
import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestbookDto guestbookDto) {

        log.info("DTO.......................");
        log.info(guestbookDto);

        Guestbook entity = dtoToEntity(guestbookDto);

        log.info(entity);

        guestbookRepository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResponseDto<GuestbookDto, Guestbook> getList(PageRequestDto requestDto) {

        Pageable pageable = requestDto.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDto);  //검색 조건 처리 추가

//        Page<Guestbook> result = guestbookRepository.findAll(pageable);   //AS-IS
        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable); //TO-BE
                                                                                        //Querydsl 사용

        Function<Guestbook, GuestbookDto> fn = (entity -> entityToDto(entity));

        return new PageResponseDto<>(result, fn);
    }

    @Override
    public GuestbookDto read(Long gno) {

//        Optional<Guestbook> result = guestbookRepository.findById(gno);
//
//        return result.isPresent() ? entityToDto(result.get()) : null;

        return entityToDto(guestbookRepository.findById(gno).orElseGet(Guestbook::new));

    }

    @Override
    public void remove(Long gno) {

        guestbookRepository.deleteById(gno);

    }

    @Transactional
    @Override
    public Long modify(GuestbookDto dto) throws Exception {

//        AS-IS
//        Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());
//
//        if (result.isPresent()) {
//
//            Guestbook entity = result.get();
//
//            entity.changeTitle(dto.getTitle());
//
//            entity.changeContent(dto.getContent());
//
//            guestbookRepository.save(entity);
//        }

        //TO-BE
        Guestbook guestbook = guestbookRepository.findById(dto.getGno()).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. gno: " + dto.getGno()));

        guestbook.changeTitle(dto.getTitle());
        guestbook.changeContent(dto.getContent());

        return guestbook.getGno();
    }

    private BooleanBuilder getSearch(PageRequestDto requestDto) {   //PageRequestDto를 파라미터로 받아 검색 조건이 있는 경우에는 conditionBuilder 변수를 생성해서 각 검색 조건을 'or'로 연결해서 처리

        String type = requestDto.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanExpression expression = qGuestbook.gno.gt(0L);   //gno > 0 조건 추가

        booleanBuilder.and(expression);

        String keyword = requestDto.getKeyword();

        if (type == null || type.trim().length() == 0) {    //검색 조건이 없는 경우
            return booleanBuilder;
        }

        //검색 조건 추가
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;

    }
}
