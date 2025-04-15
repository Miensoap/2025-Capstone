package kookmin.capstone.repository;

import kookmin.capstone.domain.log.DailyLogs;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyLogsRepository extends MongoRepository<DailyLogs, String> {
    List<DailyLogs> findByDeviceIdIn(List<String> deviceIds);

    List<DailyLogs> findByDeviceIdInAndDate(List<String> deviceIds, LocalDate date);

    List<DailyLogs> findByDeviceIdInAndDateBetween(List<String> deviceIds, LocalDate from, LocalDate to);

    List<DailyLogs> findByDeviceId(String deviceId);
}
