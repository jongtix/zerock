package com.jongtix.zerock.domain.user;

import com.jongtix.zerock.domain.BaseEntity;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)  //여러 개의 권한을 갖고, 객체의 일부로만 사용되도록 설정
                                                //@EnableJpaAuditing 설정 필요
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    public void addMemberRole(Role role) {
        roleSet.add(role);
    }

}
