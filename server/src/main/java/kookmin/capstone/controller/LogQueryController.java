package kookmin.capstone.controller;

import kookmin.capstone.controller.dto.DailyLogResponse;
import kookmin.capstone.domain.Device;
import kookmin.capstone.domain.User;
import kookmin.capstone.domain.log.DailyLogs;
import kookmin.capstone.domain.log.Sha256HashStrategy;
import kookmin.capstone.repository.DailyLogsRepository;
import kookmin.capstone.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/user/logs")
@RestController
@RequiredArgsConstructor
public class LogQueryController {

    private final DeviceRepository deviceRepository;
    private final DailyLogsRepository dailyLogsRepository;
    private final Sha256HashStrategy hashStrategy;

    @GetMapping
    public ResponseEntity<List<DailyLogResponse>> getAllLogsForDate(
            @AuthenticationPrincipal User user,
            @RequestParam LocalDate date
    ) {
        List<String> deviceIds = deviceRepository.findByOwner(user).stream()
                .map(Device::getDeviceId)
                .toList();

        List<DailyLogs> logs = dailyLogsRepository.findByDeviceIdInAndDate(
                deviceIds,
                date // Todo. 시간대 설정
        );

        List<DailyLogResponse> result = logs.stream()
                .map(log -> DailyLogResponse.from(log, hashStrategy))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<List<DailyLogResponse>> getDeviceLogs(
            @AuthenticationPrincipal User user,
            @PathVariable String deviceId
    ) {
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        if (!device.getOwner().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<DailyLogs> logs = dailyLogsRepository.findByDeviceId((deviceId));

        List<DailyLogResponse> result = logs.stream()
                .map(log -> DailyLogResponse.from(log, hashStrategy))
                .toList();

        return ResponseEntity.ok(result);
    }
}

