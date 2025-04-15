package kookmin.capstone.domain.log;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndex(def = "{'deviceId': 1, 'date': -1}")
@Document(collection = "daily_logs")
public class DailyLogs {

    @Id
    private String id;

    private String deviceId;
    private LocalDate date;

    private List<SensorLog> logs;

    public DailyLogs(String deviceId, LocalDate date, List<SensorLog> logs) {
        this.deviceId = Objects.requireNonNull(deviceId);
        this.date = Objects.requireNonNull(date);
        this.logs = logs == null ? new ArrayList<>() : new ArrayList<>(logs);
    }

    public boolean isTampered(SensorLog.HashStrategy strategy) {
        return findTamperedIndex(strategy) != -1;
    }

    /**
     * @return tampered index if found, or -1 if all logs are valid
     */
    public int findTamperedIndex(SensorLog.HashStrategy strategy) {
        for (int i = 0; i < logs.size(); i++) {
            SensorLog log = logs.get(i);
            String prev = (i == 0) ? log.getHash().prev() : logs.get(i - 1).getHash().current();
            if (prev == null) continue;

            String expected = strategy.calculate(prev, deviceId, log.getTimestamp(), log.getSensorData());
            if (!expected.equals(log.getHash().current())) {
                return i;
            }
        }
        return -1;
    }

    public SensorLog getLastLog() {
        if (logs.isEmpty()) return null;
        return logs.get(logs.size() - 1);
    }
}
