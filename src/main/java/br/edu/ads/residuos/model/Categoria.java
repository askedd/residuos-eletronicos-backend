package br.edu.ads.residuos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categoria_residuos_proj_Sandra") // nome exato da sua tabela
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria") // PK do seu SQL
    private Long idCategoria;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 50)
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @NotBlank(message = "As instruções de segurança são obrigatórias")
    @Size(max = 100)
    @Column(name = "instrucoes_seguranca", nullable = false, length = 100)
    private String instrucoesSeguranaca;
}
