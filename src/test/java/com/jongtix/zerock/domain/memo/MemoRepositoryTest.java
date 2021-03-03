package com.jongtix.zerock.domain.memo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @AfterEach
    public void tearDown() {
        memoRepository.deleteAll();
    }

    @DisplayName("테스트")
    @Test
    public void test() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + memoRepository.getClass().getName() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @DisplayName("등록_작업_테스트")
    @Test
    public void test_insert_dummies() {
        //given
        //when
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder().memoText("내용" + i).build())
                );

        //then
        assertThat(memoRepository.findAll().size()).isEqualTo(100);
    }

    @DisplayName("조회_작업_테스트")
    @Test
    public void test_select() throws Exception {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder().memoText("내용" + i).build())
                );

        //when
        Memo memo = memoRepository.findById(1L).orElseThrow(() -> new Exception());

        //then
        assertThat(memo.getMemoText()).isEqualTo("내용" + 1);
    }

    @DisplayName("전체_데이터_조회_작업_테스트")
    @Test
    public void test_select_all() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder().memoText("내용" + i).build())
                );

        //when
        List<Memo> memos = memoRepository.findAll();

        //then
        assertThat(memos.get(0).getMemoText()).isEqualTo("내용" + 1);
        assertThat(memos.get(99).getMemoText()).isEqualTo("내용" + 100);
    }

    @DisplayName("수정_작업_테스트")
    @Test
    public void test_update() throws Exception {
        //given
        String expectedText = "update text";

        Long mno = memoRepository.save(Memo.builder()
                .memoText("normal text")
                .build()).getMno();

        //when
        memoRepository.save(Memo.builder()
                .mno(mno)
                .memoText(expectedText)
                .build());

        //then
        Memo memo = memoRepository.findById(mno).orElseThrow(() -> new Exception());
        assertThat(memo.getMno()).isEqualTo(mno);
        assertThat(memo.getMemoText()).isEqualTo(expectedText);
    }

    @DisplayName("삭제_작업_테스트")
    @Test
    public void test_delete_by_id() {
        //given
        Long mno = memoRepository.save(Memo.builder()
                        .memoText("내용")
                        .build()).getMno();

        //when
        memoRepository.deleteById(mno);

        List<Memo> memos = memoRepository.findAll();

        //then
        assertThat(memos.size()).isEqualTo(0);
    }

}