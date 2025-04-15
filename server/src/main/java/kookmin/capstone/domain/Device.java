package kookmin.capstone.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {
    @Id
    private String deviceId; // ESP.getEfuseMac()

    @Column(nullable = false, unique = true)
    private String secret; // 서버에서 발급한 UUID

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @CreatedDate
    private final Instant registeredAt = Instant.now();

    public Device(String deviceId,
                  String secret,
                  User owner) {
        this.deviceId = deviceId;
        this.secret = secret;
        this.owner = owner;
    }

    public boolean matchesSecret(String providedSecret) {
        return this.secret.equals(providedSecret);
    }
}
