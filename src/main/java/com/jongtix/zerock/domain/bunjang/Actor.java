package com.jongtix.zerock.domain.bunjang;

import lombok.Data;

@Data
public class Actor {

    Long id;

    String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
