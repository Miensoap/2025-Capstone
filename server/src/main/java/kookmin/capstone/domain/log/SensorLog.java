package kookmin.capstone.domain.log;

import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensorLog {

    private String deviceId;
    private Instant timestamp;
    private Map<String, Double> sensorData;

    @Embedded
    private HashInfo hash;

    public record HashInfo(String prev, String current) {
    }

    public interface HashStrategy {
        String calculate(String prevHash,
                         String deviceId,
                         Instant timestamp,
                         Map<String, Double> data
        );
    }
} 
