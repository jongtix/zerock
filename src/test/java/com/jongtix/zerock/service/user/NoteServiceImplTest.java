package com.jongtix.zerock.service.user;

import com.jongtix.zerock.domain.user.ClubMember;
import com.jongtix.zerock.domain.user.ClubMemberRepository;
import com.jongtix.zerock.domain.user.Note;
import com.jongtix.zerock.domain.user.NoteRepository;
import com.jongtix.zerock.dto.request.NoteRequestDto;
import com.jongtix.zerock.dto.response.NoteResponseDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteServiceImplTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
        clubMemberRepository.deleteAll();
    }

    @DisplayName("DTO를_Entity로_변환")
    @Test
    void dtoToEntity() {
        //given
        Long num = 1L;
        String title = "title";
        String content = "content";
        String writerEmail = "writerEmail";

        NoteRequestDto dto = NoteRequestDto.builder()
                .num(num)
                .title(title)
                .content(content)
                .writerEmail(writerEmail)
                .build();

        //when
        Note note = noteService.dtoToEntity(dto);

        //then
        assertThat(note.getNum()).isEqualTo(num);
        assertThat(note.getTitle()).isEqualTo(title);
        assertThat(note.getContent()).isEqualTo(content);
        assertThat(note.getWriter().getEmail()).isEqualTo(writerEmail);
    }

    @DisplayName("Entity를_DTO로_변환")
    @Test
    void entityToDto() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        Long num = 1L;
        String title = "title";
        String content = "content";

        String email = "email";
        ClubMember clubMember = ClubMember.builder()
                        .email(email)
                        .build();

        Note entity = Note.builder()
                .num(num)
                .title(title)
                .content(content)
                .writer(clubMember)
                .build();

        //when
        NoteResponseDto dto = noteService.entityToDto(entity);

        //then
        assertThat(dto.getNum()).isEqualTo(num);
        assertThat(dto.getTitle()).isEqualTo(title);
        assertThat(dto.getContent()).isEqualTo(content);
        assertThat(dto.getWriterEmail()).isEqualTo(email);
    }

    @DisplayName("노트_등록")
    @Test
    void register() {
        //given
        String title = "title";
        String content = "content";
        String writerEmail = "writerEmail";
        clubMemberRepository.save(
                ClubMember.builder()
                        .email(writerEmail)
                        .build()
        );

        NoteRequestDto requestDto = NoteRequestDto.builder()
                .title(title)
                .content(content)
                .writerEmail(writerEmail)
                .build();

        //when
        Long num = noteService.register(requestDto);

        //then
        Note result = noteRepository.findById(num).orElseThrow(() -> new IllegalArgumentException("해당하는 노트가 없습니다. num: " + num));
        assertThat(result.getNum()).isEqualTo(num);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getWriter().getEmail()).isEqualTo(writerEmail);
    }

    @DisplayName("노트_번호로_선택")
    @Test
    void get() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        String content = "content";
        String writerEmail = "writerEmail";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        //when
        NoteResponseDto result = noteService.get(num);

        //then
        assertThat(result.getNum()).isEqualTo(num);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getRegDate()).isAfter(now);
        assertThat(result.getModDate()).isAfter(now);
    }

    @Test
    void modify() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        String content = "content";
        String writerEmail = "writerEmail";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";
        NoteRequestDto noteRequestDto = NoteRequestDto.builder()
                .num(num)
                .title(expectedTitle)
                .content(expectedContent)
                .writerEmail(writerEmail)
                .build();

        //when
        noteService.modify(noteRequestDto);

        //then
        Note result = noteRepository.findById(num).orElseThrow(() -> new IllegalArgumentException("해당하는 노트가 없습니다. num: " + num));
        assertThat(result.getNum()).isEqualTo(num);
        assertThat(result.getTitle()).isEqualTo(expectedTitle);
        assertThat(result.getContent()).isEqualTo(expectedContent);
        assertThat(result.getWriter().getEmail()).isEqualTo(writerEmail);
    }

    @DisplayName("노트_삭제")
    @Test
    void remove() {
        //given
        String title = "title";
        String content = "content";
        String writerEmail = "writerEmail";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        //when
        noteService.remove(num);

        //then
        assertThrows(NoSuchElementException.class, () -> noteRepository.findById(num).orElseThrow(() -> new NoSuchElementException("해당하는 노트가 없습니다. num: " + num)));
    }

    @DisplayName("노트_리스트_가져오기")
    @Test
    void getAllWithWriter() {
        //given
        String writerEmail = "writerEmail";
        ClubMember writer = clubMemberRepository.save(
                ClubMember.builder()
                        .email(writerEmail)
                        .build()
        );

        String title1 = "title1";
        String content1 = "content1";
        noteRepository.save(
                Note.builder()
                        .title(title1)
                        .content(content1)
                        .writer(writer)
                        .build()
        );

        String title2 = "title2";
        String content2 = "content2";
        noteRepository.save(
                Note.builder()
                        .title(title2)
                        .content(content2)
                        .writer(writer)
                        .build()
        );

        //when
        List<NoteResponseDto> result = noteService.getAllWithWriter(writerEmail);

        //then
        assertThat(result.get(0).getTitle()).isEqualTo(title1);
        assertThat(result.get(0).getContent()).isEqualTo(content1);
        assertThat(result.get(0).getWriterEmail()).isEqualTo(writerEmail);
        assertThat(result.get(1).getTitle()).isEqualTo(title2);
        assertThat(result.get(1).getContent()).isEqualTo(content2);
        assertThat(result.get(1).getWriterEmail()).isEqualTo(writerEmail);
    }
}