package kookmin.capstone.controller.dto;

import kookmin.capstone.domain.log.DailyLogs;
import kookmin.capstone.domain.log.SensorLog;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DailyLogUploadRequest {
    private LocalDate date;
    private List<SensorLog> logs;

    public DailyLogs toEntity(String deviceId) {
        return new DailyLogs(deviceId, date, logs);
    }
}
