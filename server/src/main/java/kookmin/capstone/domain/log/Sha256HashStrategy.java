package kookmin.capstone.domain.log;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class Sha256HashStrategy implements SensorLog.HashStrategy {

    @Override
    public String calculate(String prevHash,
                            String deviceId,
                            Instant timestamp,
                            Map<String, Double> data
    ) {
        StringBuilder input = new StringBuilder();

        input.append(prevHash != null ? prevHash : "");
        input.append(deviceId);
        input.append(timestamp.toString());

        String sortedData = new TreeMap<>(data).entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));
        input.append(sortedData);

        return sha256(input.toString());
    }

    private String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
} 
