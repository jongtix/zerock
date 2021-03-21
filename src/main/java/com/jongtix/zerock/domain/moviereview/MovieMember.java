package com.jongtix.zerock.domain.moviereview;

import com.jongtix.zerock.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity(name = "MovieMember")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "m_member")
public class MovieMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    private String email;

    private String password;

    private String nickname;

}
