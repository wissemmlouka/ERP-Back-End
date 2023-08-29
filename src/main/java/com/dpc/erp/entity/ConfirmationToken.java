package com.dpc.erp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String confirmationToken;
    @CreationTimestamp
    private LocalDateTime creationDate;
    private String action;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationToken(User user, String action) {
        this.user = user;
        this.action = action;
        this.confirmationToken = UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
    }
}
