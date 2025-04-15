package kookmin.capstone.controller;

import kookmin.capstone.controller.dto.DeviceRegisterRequest;
import kookmin.capstone.controller.dto.DeviceRegisterResponse;
import kookmin.capstone.domain.Device;
import kookmin.capstone.domain.User;
import kookmin.capstone.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/user/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;

    @PostMapping("/register")
    public ResponseEntity<DeviceRegisterResponse> registerDevice(
            @RequestBody DeviceRegisterRequest request,
            @AuthenticationPrincipal User user
    ) {
        String deviceId = request.deviceId();

        // 이미 등록된 디바이스가 있으면 기존 시크릿 키 반환
        Optional<Device> existing = deviceRepository.findById(deviceId);
        if (existing.isPresent()) {
            Device device = existing.get();

//            // 유저 일치 여부 확인 (같은 유저의 디바이스만 허용)
//            if (!device.getOwner().getId().equals(user.getId())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 남의 디바이스 접근 막기
//            }

            return ResponseEntity.ok(new DeviceRegisterResponse(device.getDeviceId(), device.getSecret()));
        }

        // 새 디바이스 등록
        Device device = new Device(deviceId, UUID.randomUUID().toString(), user);
        deviceRepository.save(device);

        return ResponseEntity.ok(new DeviceRegisterResponse(device.getDeviceId(), device.getSecret()));
    }


    @GetMapping("my")
    public List<Device> getMyDevice(
            @AuthenticationPrincipal User user
    ) {
        return deviceRepository.findByOwner(user);
    }
}
