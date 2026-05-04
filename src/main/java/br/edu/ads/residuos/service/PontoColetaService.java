package br.edu.ads.residuos.service;

import br.edu.ads.residuos.model.PontoColeta;
import br.edu.ads.residuos.model.UsuarioInteracao;
import br.edu.ads.residuos.repository.PontoColetaRepository;
import br.edu.ads.residuos.repository.UsuarioInteracaoRepository;
import br.edu.ads.residuos.util.HaversineUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PontoColetaService {

    private final PontoColetaRepository pontoRepository;
    private final UsuarioInteracaoRepository interacaoRepository;

    /**
     * Retorna os pontos de coleta mais próximos do usuário.
     *
     * ATENÇÃO: usa ponto.getLgn() (não getLng()) — coluna "lgn" do seu SQL.
     *
     * @param latUsuario  Latitude capturada pelo navigator.geolocation
     * @param lgnUsuario  Longitude capturada pelo navigator.geolocation
     * @param raioMaxKm   Raio máximo em km (null = sem limite)
     * @return Lista ordenada do mais próximo ao mais distante
     */
    public List<PontoColeta> buscarProximos(double latUsuario, double lgnUsuario,
                                             Double raioMaxKm) {
        List<PontoColeta> todos = pontoRepository.findAll();

        // Calcula distância de cada ponto até o usuário (Fórmula de Haversine)
        // Usa getLgn() porque a coluna no seu banco é "lgn"
        todos.forEach(ponto -> {
            double dist = HaversineUtil.calcularDistancia(
                    latUsuario, lgnUsuario,
                    ponto.getLat(), ponto.getLgn()   // <-- getLgn(), não getLng()
            );
            ponto.setDistanciaKm(dist);
        });

        // Filtra por raio e ordena
        List<PontoColeta> resultado = todos.stream()
                .filter(p -> raioMaxKm == null || p.getDistanciaKm() <= raioMaxKm)
                .sorted(Comparator.comparingDouble(PontoColeta::getDistanciaKm))
                .toList();

        // Registra a interação apontando para o ponto mais próximo encontrado
        // (seu SQL exige id_ponto NOT NULL na tabela de interações)
        if (!resultado.isEmpty()) {
            registrarInteracao(resultado.get(0), lgnUsuario, latUsuario);
        }

        return resultado;
    }

    public List<PontoColeta> listarTodos() {
        return pontoRepository.findAll();
    }

    public PontoColeta salvar(PontoColeta ponto) {
        return pontoRepository.save(ponto);
    }

    public void deletar(Long id) {
        pontoRepository.deleteById(id);
    }

    // -------------------------------------------------------

    private void registrarInteracao(PontoColeta pontoMaisProximo,
                                     double lgn, double lat) {
        UsuarioInteracao interacao = new UsuarioInteracao();

        // seu SQL: id_ponto NOT NULL — FK para pontos_coleta_projeto_Sandra
        interacao.setPontoConsultado(pontoMaisProximo);

        // Região estimada com as coordenadas do usuário
        interacao.setRegiaoEstimada(
                String.format("Lat: %.5f | Lgn: %.5f", lat, lgn)
        );

        interacaoRepository.save(interacao); // data_consulta preenchida pelo @PrePersist
    }
}
