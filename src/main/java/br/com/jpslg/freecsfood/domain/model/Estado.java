package br.com.jpslg.freecsfood.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estado extends EntidadeBase {

    @NotBlank @Column(nullable = false)
    private String nome;
}
