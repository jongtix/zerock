package com.jongtix.zerock.service;

import com.jongtix.zerock.domain.guestbook.Guestbook;
import com.jongtix.zerock.domain.guestbook.GuestbookRepository;
import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

        Page<Guestbook> result = guestbookRepository.findAll(pageable);

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

    @Override
    public void modify(GuestbookDto dto) throws Exception {

        Optional<Guestbook> result = guestbookRepository.findById(dto.getGno());

        if (result.isPresent()) {

            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());

            entity.changeContent(dto.getContent());

            guestbookRepository.save(entity);
        }

//        Guestbook guestbook = guestbookRepository.findById(dto.getGno()).orElseThrow(() -> new Exception());
//
//        guestbookRepository.save(dtoToEntity(dto));
    }
}
