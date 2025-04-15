package kookmin.capstone.controller.dto;

import kookmin.capstone.domain.log.DailyLogs;
import kookmin.capstone.domain.log.SensorLog;

import java.time.LocalDate;
import java.util.List;

public record DailyLogResponse(
        String deviceId,
        LocalDate date,
        boolean tampered,
        int logCount,
        List<SensorLog> logs
) {
    public static DailyLogResponse from(DailyLogs dailyLogs, SensorLog.HashStrategy strategy) {
        return new DailyLogResponse(
                dailyLogs.getDeviceId(),
                dailyLogs.getDate(),
                dailyLogs.isTampered(strategy),
                dailyLogs.getLogs().size(),
                dailyLogs.getLogs()
        );
    }
}
