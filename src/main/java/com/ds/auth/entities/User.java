package com.ds.auth.entities;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@javax.persistence.Entity
@Table(name = "users")
@Data
public class User implements UserDetails, IEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @NotBlank(message = "O nome é obrigatório.")
    String username;

    @Column(name = "user_password")
    String password;

    @NotNull(message = "O usuário precisa de um perfil de informações.")
    @Valid
    @OneToOne
    @JoinColumn(name = "role_id")
    Role role;

    @Column
    public boolean isAccountNonExpired;

    @Column
    boolean isAccountNonLocked;

    @Column
    boolean isCredentialsNonExpired;

    @Column
    boolean isEnabled;

    @Transient
    Collection<Authority> authorities;

    @Override
    public Collection<Authority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String toString() {
        return "User(" + this.id + ")";
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

}
