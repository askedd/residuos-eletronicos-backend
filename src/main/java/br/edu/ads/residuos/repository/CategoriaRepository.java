package br.edu.ads.residuos.repository;

import br.edu.ads.residuos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JpaRepository<Entidade, TipoDaPK>
// A PK no seu SQL é "id_categoria" (Long)
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Spring gera: SELECT * FROM categoria_residuos_proj_Sandra WHERE nome = ?
    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);
}
