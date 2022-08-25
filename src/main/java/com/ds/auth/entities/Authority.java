package com.ds.auth.entities;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority, IEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Override
    public String getAuthority() {
        return this.name;
    }

}
