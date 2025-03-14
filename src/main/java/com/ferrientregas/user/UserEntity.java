package com.ferrientregas.user;

import com.ferrientregas.audit.Auditable;
import com.ferrientregas.role.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
public class UserEntity extends Auditable implements UserDetails {

    @Column(length = 50, nullable = false)
    private String firstNames;
    @Column(length = 50, nullable = false)
    private String lastNames;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private UUID profileImage;
    private Boolean emailConfirmed;
    @Column(name = "token", unique = true)
    private String token;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RoleEntity> roles;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
        this.emailConfirmed = false;
        this.token = String.valueOf(new Random().nextInt(90000) + 10000);
    }

    @PreUpdate
    protected void onUpdate() {
        this.setCreatedAt(now());
        if(this.isDeleted()) this.setDeletedAt(now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add roles as GrantedAuthority
        this.roles.forEach(role -> {
            // Add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
