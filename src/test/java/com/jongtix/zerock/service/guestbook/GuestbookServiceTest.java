package com.jongtix.zerock.service.guestbook;

import com.jongtix.zerock.domain.guestbook.Guestbook;
import com.jongtix.zerock.domain.guestbook.GuestbookRepository;
import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import com.jongtix.zerock.service.guestbook.GuestbookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GuestbookServiceTest {

    @Autowired
    private GuestbookService guestbookService;

    @Autowired
    private GuestbookRepository guestbookRepository;

    @BeforeEach
    void setup() {
        IntStream.rangeClosed(1, 150)
                .forEach(
                        i -> guestbookRepository.save(
                                Guestbook.builder()
                                        .title("title" + i)
                                        .content("content" + i)
                                        .writer("writer" + i)
                                        .build()
                        )
                );
    }

    @AfterEach
    void tearDown() {
        guestbookRepository.deleteAll();
    }

    @DisplayName("service_기본_테스트")
    @Test
    void test_register() {
        //given
        GuestbookDto guestbookDto = GuestbookDto.builder()
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        //when
        //then
        Long gno = guestbookService.register(guestbookDto);
    }

    @DisplayName("조건이_있을_경우_리스트_가져오기_테스트")
    @Test
    void test_list_with_condition() {
        //given
        String type = "t";
        String keyword = "1";

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1)
                .size(10)
                .type(type)
                .keyword(keyword)
                .build();

        //when
        PageResponseDto pageResponseDto = guestbookService.getList(pageRequestDto);

        //then
        assertThat(pageResponseDto.getDtoList().size()).isEqualTo(10);
        assertThat(((GuestbookDto) pageResponseDto.getDtoList().get(0)).getTitle()).contains(keyword);
        assertThat(((GuestbookDto) pageResponseDto.getDtoList().get(9)).getTitle()).contains(keyword);
        assertThat(pageResponseDto.isPrev()).isEqualTo(false);
        assertThat(pageResponseDto.isNext()).isEqualTo(false);
        assertThat(pageResponseDto.getTotalPage()).isEqualTo(7);
    }

    @DisplayName("조건이_없을_경우_리스트_가져오기_테스트")
    @Test
    void test_list() {
        //given
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(1)
                .size(10)
                .build();

        //when
        PageResponseDto pageResponseDto = guestbookService.getList(pageRequestDto);

        //then
        assertThat(pageResponseDto.getDtoList().size()).isEqualTo(10);
        assertThat(((GuestbookDto) pageResponseDto.getDtoList().get(0)).getTitle()).isEqualTo("title" + 150);
        assertThat(((GuestbookDto) pageResponseDto.getDtoList().get(9)).getTitle()).isEqualTo("title" + 141);
        assertThat(pageResponseDto.isPrev()).isEqualTo(false);
        assertThat(pageResponseDto.isNext()).isEqualTo(true);
        assertThat(pageResponseDto.getTotalPage()).isEqualTo(15);
    }

    @DisplayName("엔티티에서_DTO_전환_시_null_처리_테스트")
    @Test
    void entityToDto_with_null_test() {
        GuestbookDto dto = null;
        try {
            //when
            dto = guestbookService.entityToDto(null);
        } catch (NullPointerException npe) {
            assertThat(dto).isNull();
        }

    }

}