package br.edu.ads.residuos.repository;

import br.edu.ads.residuos.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// PK é "id_ponto" (Long)
@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    // Filtra pontos por id da categoria
    // Spring gera: SELECT * FROM pontos_coleta_projeto_Sandra WHERE id_categoria = ?
    List<PontoColeta> findByCategoriaIdCategoria(Long idCategoria);

    // Verifica duplicata pelo endereço (coluna UNIQUE do seu SQL)
    boolean existsByEndereco(String endereco);
}
