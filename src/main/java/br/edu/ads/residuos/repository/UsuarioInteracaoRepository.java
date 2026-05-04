package br.edu.ads.residuos.repository;

import br.edu.ads.residuos.model.UsuarioInteracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// PK é "id_consulta" (Long)
@Repository
public interface UsuarioInteracaoRepository extends JpaRepository<UsuarioInteracao, Long> {

    // Busca todas as interações de um ponto específico (útil para métricas)
    List<UsuarioInteracao> findByPontoConsultadoIdPonto(Long idPonto);
}
