package br.edu.ads.residuos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios_interacao_proj_Sandra") // nome exato da sua tabela
public class UsuarioInteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta") // PK do seu SQL
    private Long idConsulta;

    // FK para pontos_coleta_projeto_Sandra — seu SQL registra QUAL ponto foi consultado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ponto", nullable = false)
    @NotNull(message = "O ponto consultado é obrigatório")
    private PontoColeta pontoConsultado;

    // SQLite armazena datas como TEXT. O Spring formata automaticamente.
    @Column(name = "data_consulta", nullable = false)
    private String dataConsulta;

    @Column(name = "regiao_estimada", length = 150)
    private String regiaoEstimada;

    @PrePersist
    public void prePersist() {
        // Reproduz o DEFAULT (datetime('now')) do seu SQL
        this.dataConsulta = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
