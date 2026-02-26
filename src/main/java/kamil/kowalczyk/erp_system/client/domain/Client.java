package kamil.kowalczyk.erp_system.client.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    private String firstName;
    private String lastName;

    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Client(Long userId, String firstName, String lastName, String phoneNumber) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    @PrePersist
    public void PrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
