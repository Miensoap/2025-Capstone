package kookmin.capstone.controller;

import kookmin.capstone.controller.dto.DailyLogUploadRequest;
import kookmin.capstone.domain.Device;
import kookmin.capstone.service.DailyLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/device/logs")
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final DailyLogService dailyLogService;

    @PostMapping("/daily")
    public ResponseEntity<String> uploadDailyLogs(@RequestBody DailyLogUploadRequest request,
                                                  @AuthenticationPrincipal Device device) {

        dailyLogService.uploadDailyLogs(request, device.getDeviceId());
        return ResponseEntity.ok().build();
    }
}
