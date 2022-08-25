package com.ds.auth.entities;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "roles")
@Data
public class Role implements Serializable, IEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    @NotBlank(message = "Nome do perfil não pode estar em branco")
    String name;

}
