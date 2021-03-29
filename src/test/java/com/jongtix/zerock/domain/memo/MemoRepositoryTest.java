package com.jongtix.zerock.domain.memo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //테스트 인스턴스의 생명주기 설정
//                                                //@BeforeAll이나 @AfterAll의 경우 static으로 선언하지 않으면 에러 발생하는데 이를 해결 가능
class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

//    @BeforeAll
//    public void setup() {
//        IntStream.rangeClosed(1, 100)
//                .forEach(i ->
//                        memoRepository.save(Memo.builder()
//                                .memoText("내용" + i)
//                                .build())
//                );
//    }

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
                        memoRepository.save(Memo.builder().memoText("memoText" + i).build())
                );

        //then
        assertThat(memoRepository.findAll().size()).isEqualTo(100);
    }

    @DisplayName("조회_작업_테스트")
    @Test
    public void test_select() throws Exception {
        //given
        Long mno = memoRepository.save(Memo.builder().memoText("NEW memoText").build()).getMno();

        //when
        Memo memo = memoRepository.findById(mno).orElseThrow(() -> new Exception());

        //then
        assertThat(memo.getMemoText()).isEqualTo("NEW memoText");
    }

    @DisplayName("전체_데이터_조회_작업_테스트")
    @Test
    public void test_select_all() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder().memoText("memoText" + i).build())
                );

        //when
        List<Memo> memos = memoRepository.findAll();

        //then
        assertThat(memos.get(0).getMemoText()).isEqualTo("memoText" + 1);
        assertThat(memos.get(99).getMemoText()).isEqualTo("memoText" + 100);
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
                        .memoText("memoText")
                        .build()).getMno();

        //when
        memoRepository.deleteById(mno);

        Memo memo = memoRepository.findById(mno).orElseGet(Memo::new);

        //then
        assertThat(memo.getMno()).isNull();
        assertThat(memo.getMemoText()).isNull();
    }

    @DisplayName("기본_페이징_테스트")
    @Test
    public void paging_test_default() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder()
                                .memoText("memoText" + i)
                                .build())
                );

        Pageable pageable = PageRequest.of(0, 10);	/** ((주의)) 페이지 처리는 반드시 0 부터 시작 */

        //when
        Page<Memo> result = memoRepository.findAll(pageable);   //Page 타입은 한번에 리스트 + count 까지 select 한다

        //then
        assertThat(result.stream().count()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(10);   //총 몇 페이지
        assertThat(result.getTotalElements()).isEqualTo(100);   //전체 개수
        assertThat(result.getNumber()).isEqualTo(0);    //현재 페이지 번호의 시작
        assertThat(result.getSize()).isEqualTo(10); //페이지당 데이터 수
        assertThat(result.hasNext()).isTrue();  //다음 페이지 존재 여부
        assertThat(result.isFirst()).isTrue();  //시작 페이지 여부

        assertThat(result.getContent().get(0).getMemoText()).isEqualTo("memoText" + 1);
    }

    @DisplayName("정렬_페이징_테스트")
    @Test
    void paging_test_sort() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder()
                                .memoText("memoText" + i)
                                .build())
                );
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);
//        Pageable pageable = PageRequest.of(0, 10, sort1);
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        //when
        Page<Memo> result = memoRepository.findAll(pageable);

        //then
        assertThat(result.getContent().get(0).getMemoText()).isEqualTo("memoText" + 100);
    }

    @DisplayName("쿼리_메서드_기능_테스트")
    @Test
    void query_method_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder()
                                .memoText("memoText" + i)
                                .build())
                );
        Long startMno = (long) memoRepository.getMinMno() - 1L;

        //when
        List<Memo> memos = memoRepository.findByMnoBetweenOrderByMnoDesc(startMno + 70L,startMno + 80L);

        //then
        assertThat(memos.size()).isEqualTo(11);
        assertThat(memos.get(0).getMemoText()).isEqualTo("memoText" + 80);
        assertThat(memos.get(10).getMemoText()).isEqualTo("memoText" + 70);
    }

    @DisplayName("쿼리_메서드_with_pageable_기능_테스트")
    @Test
    void query_method_with_pageable_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                    memoRepository.save(Memo.builder()
                            .memoText("memoText" + i)
                            .build())
                );

        Long startMno = (long) memoRepository.getMinMno() - 1L;
        Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());


        //when
        List<Memo> memos = memoRepository.findByMnoBetween(startMno + 10L, startMno + 50L, pageable);

        //then
        assertThat(memos.size()).isEqualTo(10);
        assertThat(memos.get(0).getMemoText()).isEqualTo("memoText" + 40);
        assertThat(memos.get(9).getMemoText()).isEqualTo("memoText" + 31);
    }

    @DisplayName("쿼리_메서드_삭제_기능_테스트")
    @Transactional  //deleteBy...의 경우 select와  각 엔티티를 삭제하는 작업이 함께 이루어지기 때문에 필수
    @Test
    void query_method_delete_by_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder()
                                .memoText("memoText" + i)
                                .build())
                );

        Long startMno = (long) memoRepository.getMinMno() - 1L;

        //when
        memoRepository.deleteMemoByMnoLessThan(startMno + 20L);

        //then
        List<Memo> memos = memoRepository.findAll();
        assertThat(memos.size()).isEqualTo(81);
        assertThat(memos.get(0).getMemoText()).isEqualTo("memoText" + 20);
    }

    @DisplayName("쿼리_어노테이션_조회_기능_테스트")
    @Test
    void query_annotation_select_list_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(i ->
                        memoRepository.save(Memo.builder()
                                .memoText("memoText" + i)
                                .build())
                );

        //when
        List<Memo> memos = memoRepository.getListDesc();

        //then
        assertThat(memos.size()).isEqualTo(100);
        assertThat(memos.get(0).getMemoText()).isEqualTo("memoText" + 100);
        assertThat(memos.get(99).getMemoText()).isEqualTo("memoText" + 1);
    }

    @DisplayName("쿼리_어노테이션_수정_기능_테스트")
    @Test
    void query_annotation_update_test() throws Exception {
        //given
        String expectedText = "updatedText";

        Long mno = memoRepository.save(
                        Memo.builder()
                                .memoText("memoText")
                                .build()
                ).getMno();

        //when
        memoRepository.updateMemoText(mno, expectedText);

        //then
        Memo memo = memoRepository.findById(mno).orElseThrow(() -> new Exception());
        assertThat(memo.getMno()).isEqualTo(mno);
        assertThat(memo.getMemoText()).isEqualTo(expectedText);
    }

    @DisplayName("쿼리_어노테이션_수정_기능_테스트_2")
    @Test
    void query_annotation_update_text_2() throws Exception {
        //given
        String expectedText = "updatedText";

        Long mno = memoRepository.save(
                Memo.builder()
                        .memoText("memoText")
                        .build()
        ).getMno();

        //when
        memoRepository.updateMemoText(
                Memo.builder()
                        .mno(mno)
                        .memoText(expectedText)
                        .build());

        //then
        Memo memo = memoRepository.findById(mno).orElseThrow(() -> new Exception());
        assertThat(memo.getMno()).isEqualTo(mno);
        assertThat(memo.getMemoText()).isEqualTo(expectedText);
    }

    @DisplayName("페이징_추가된_쿼리_어노테이션_조회_기능_테스트")
    @Test
    void query_annotation_select_with_paging_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> memoRepository.save(
                                Memo.builder()
                                        .memoText("memoText" + i)
                                        .build()
                        )
                );
        Long startMno = (long) memoRepository.getMinMno() - 1L;

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<Memo> memos = memoRepository.getListWithQuery(startMno + 50L, pageable);

        //then
        assertThat(memos.getTotalPages()).isEqualTo(5);
        assertThat(memos.getTotalElements()).isEqualTo(50);
        assertThat(memos.getNumber()).isEqualTo(0);
        assertThat(memos.getSize()).isEqualTo(10);
        assertThat(memos.hasNext()).isTrue();
        assertThat(memos.isFirst()).isTrue();
    }

    @DisplayName("오브젝트로_결과를_받는_쿼리_어노테이션_조회_기능_테스트")
    @Test
    void query_annotation_select_object_test() {
        //given
        IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> memoRepository.save(
                                Memo.builder()
                                        .memoText("memoText" + i)
                                        .build()
                        )
                );

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<Object[]> memos = memoRepository.getListWithQueryObject(50L, pageable);

        //then
        assertThat(memos.getTotalPages()).isEqualTo(5);
        assertThat(memos.getTotalElements()).isEqualTo(50);
        assertThat(memos.getNumber()).isEqualTo(0);
        assertThat(memos.getSize()).isEqualTo(10);
        assertThat(memos.hasNext()).isTrue();
        assertThat(memos.isFirst()).isTrue();

//        System.out.println(memos.getContent().getClass());
//        Arrays.stream(memos.getContent().get(0)).forEach(
//                str -> System.out.println(str)
//        );
    }

}