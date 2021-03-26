package com.jongtix.zerock.domain.bunjang;

import lombok.Data;

import java.util.Map;

@Data
public class Result {

    String login;

    Map<String, Integer> events;

    public Result(String login, Map<String, Integer> events) {
        this.login = login;
        this.events = events;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Map<String, Integer> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Integer> events) {
        this.events = events;
    }

}
