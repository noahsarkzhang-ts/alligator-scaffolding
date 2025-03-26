package org.noahsark.online.pojo.po;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class UserOnlineStat {
    public final static Short SUBJECT_TYPE_DEVICE = 1;

    private Long customerId;
    private String deviceSn;
    private LocalDate date;
    private Long minutes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOnlineStat that = (UserOnlineStat) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(deviceSn, that.deviceSn) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, deviceSn, date);
    }
}
