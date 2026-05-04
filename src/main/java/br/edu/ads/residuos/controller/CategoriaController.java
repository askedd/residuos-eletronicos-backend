package br.edu.ads.residuos.controller;

import br.edu.ads.residuos.model.Categoria;
import br.edu.ads.residuos.repository.CategoriaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaRepository repository;

    /**
     * GET /api/categorias
     */
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * POST /api/categorias
     *
     * Body JSON:
     * {
     *   "nome": "Baterias e Pilhas",
     *   "descricao": "...",
     *   "instrucoesSeguranaca": "Não perfurar..."
     * }
     */
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Categoria categoria) {
        if (repository.existsByNome(categoria.getNome())) {
            return ResponseEntity.badRequest()
                    .body("Categoria '" + categoria.getNome() + "' já cadastrada.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(categoria));
    }

    /**
     * DELETE /api/categorias/{id}
     * O id aqui é o id_categoria do seu SQL.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
