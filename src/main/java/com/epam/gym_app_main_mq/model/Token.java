package com.epam.gym_app_main_mq.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TOKENS")
public class Token {

    @Id
    @GeneratedValue
    @Column(name = "TOKEN_ID")
    public Integer id;

    @Column(name = "TOKEN", unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOKEN_TYPE")
    public TokenType tokenType = TokenType.BEARER;

    @Column(name = "REVOKED")
    public boolean revoked;

    @Column(name = "EXPIRED")
    public boolean expired;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
    public User user;
}
