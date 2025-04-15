package kookmin.capstone.repository;

import kookmin.capstone.domain.Device;
import kookmin.capstone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByOwner(User owner);

    Optional<Device> findByDeviceId(String deviceId);
}
