package br.edu.ads.residuos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(
    name = "pontos_coleta_projeto_Sandra", // nome exato da sua tabela
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"endereco"}),        // UNIQUE do seu SQL
        @UniqueConstraint(columnNames = {"lat", "lgn"})       // UNIQUE (lat, lgn) do seu SQL
    }
)
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ponto") // PK do seu SQL
    private Long idPonto;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 300)
    @Column(name = "endereco", nullable = false, unique = true, length = 300)
    private String endereco;

    @NotNull(message = "A latitude é obrigatória")
    @Column(name = "lat", nullable = false)
    private Double lat;

    // ATENÇÃO: no seu SQL a coluna se chama "lgn" (sem o segundo 'n')
    @NotNull(message = "A longitude é obrigatória")
    @Column(name = "lgn", nullable = false)
    private Double lgn;

    // FK para categoria_residuos_proj_Sandra
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Campo calculado em runtime — não é salvo no banco
    @Transient
    private Double distanciaKm;
}
