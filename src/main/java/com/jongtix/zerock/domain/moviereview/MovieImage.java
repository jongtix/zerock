package com.jongtix.zerock.domain.moviereview;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "movie")
public class MovieImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;    //java.util.UUID를 이용해서 고유한 번호를 생성해 사용

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

}
