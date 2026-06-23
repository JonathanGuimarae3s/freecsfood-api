package br.com.jpslg.freecsfood.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Cozinha extends EntidadeBase {

    @NotBlank @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "cozinha", fetch = FetchType.LAZY)
    private Set<Restaurante> restaurantes = new LinkedHashSet<>();
}
