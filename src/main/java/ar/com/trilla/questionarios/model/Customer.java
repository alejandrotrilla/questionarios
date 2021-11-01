package ar.com.trilla.questionarios.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Cacheable
public class Customer extends PanacheEntity {
    @NotBlank(message = "Name may not be blank")
    @Column(unique = true, nullable = false)
    public String name;

    public String description;
}
