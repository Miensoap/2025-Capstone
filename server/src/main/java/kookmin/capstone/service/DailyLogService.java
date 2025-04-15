package kookmin.capstone.service;

import kookmin.capstone.controller.dto.DailyLogUploadRequest;
import kookmin.capstone.domain.log.DailyLogs;
import kookmin.capstone.repository.DailyLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogsRepository repository;

    public void uploadDailyLogs(DailyLogUploadRequest request, String deviceId) {
        DailyLogs dailyLogs = request.toEntity(deviceId);
        repository.save(dailyLogs);
    }
}
