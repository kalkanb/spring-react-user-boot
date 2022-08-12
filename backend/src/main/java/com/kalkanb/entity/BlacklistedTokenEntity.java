package com.kalkanb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist", uniqueConstraints = {@UniqueConstraint(columnNames = "token")})
@Getter
@Setter
@NoArgsConstructor
public class BlacklistedTokenEntity {

    @Id
    @SequenceGenerator(name = "token_blacklist_seq", sequenceName = "token_blacklist_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_blacklist_seq")
    private Long id;

    @Column(name = "token", length = 1024)
    private String token;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
