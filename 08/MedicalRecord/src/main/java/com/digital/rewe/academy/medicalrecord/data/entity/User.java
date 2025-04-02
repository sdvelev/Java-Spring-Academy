package com.digital.rewe.academy.medicalrecord.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements UserDetails {

    @Column(name = "username", length = 30, unique = true)
    @NotBlank(message = "The username of a user cannot be null, empty or blank")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "The password of a user cannot be null, empty or blank")
    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Role> authorities;

    @Column(name = "account_non_expired", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean accountNonExpired = true;

    @Column(name = "account_non_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean credentialsNonExpired = true;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled = true;
}