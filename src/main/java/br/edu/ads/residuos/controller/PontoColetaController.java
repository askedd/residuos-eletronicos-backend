package br.edu.ads.residuos.controller;

import br.edu.ads.residuos.model.PontoColeta;
import br.edu.ads.residuos.service.PontoColetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PontoColetaController {

    private final PontoColetaService service;

    /**
     * GET /api/pontos
     * Lista todos os pontos cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<PontoColeta>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * GET /api/pontos/proximos?lat=-23.55&lgn=-46.63&raio=10
     *
     * Parâmetro é "lgn" (igual ao nome da coluna no seu banco).
     * O frontend deve enviar: ?lat=...&lgn=...
     */
    @GetMapping("/proximos")
    public ResponseEntity<List<PontoColeta>> buscarProximos(
            @RequestParam double lat,
            @RequestParam double lgn,           // "lgn" igual ao seu SQL
            @RequestParam(required = false) Double raio
    ) {
        return ResponseEntity.ok(service.buscarProximos(lat, lgn, raio));
    }

    /**
     * POST /api/pontos
     * Cadastra um novo ponto.
     *
     * Body JSON esperado:
     * {
     *   "nome": "Poupatempo Sé",
     *   "endereco": "Praça do Carmo, s/n",
     *   "lat": -23.5489,
     *   "lgn": -46.6338,
     *   "categoria": { "idCategoria": 1 }
     * }
     */
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PontoColeta ponto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(ponto));
    }

    /**
     * DELETE /api/pontos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
