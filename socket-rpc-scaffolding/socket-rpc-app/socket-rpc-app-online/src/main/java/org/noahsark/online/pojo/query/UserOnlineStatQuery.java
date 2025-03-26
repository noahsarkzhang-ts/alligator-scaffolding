package org.noahsark.online.pojo.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserOnlineStatQuery {
    private String deviceSn;

    private LocalDate from;

    private LocalDate to;
}
