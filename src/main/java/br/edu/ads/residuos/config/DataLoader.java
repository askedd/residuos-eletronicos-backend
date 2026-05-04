package br.edu.ads.residuos.config;

import br.edu.ads.residuos.model.Categoria;
import br.edu.ads.residuos.model.PontoColeta;
import br.edu.ads.residuos.repository.CategoriaRepository;
import br.edu.ads.residuos.repository.PontoColetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popula o banco com dados iniciais ao iniciar a aplicação (somente se vazio).
 * Os nomes das tabelas são resolvidos pelo @Table nas entidades.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepo;
    private final PontoColetaRepository pontoRepo;

    @Override
    public void run(String... args) {
        if (categoriaRepo.count() > 0) return; // Evita duplicatas

        // --- Categorias (agora com campo "nome" conforme seu SQL) ---
        Categoria baterias = criarCategoria(
                "Baterias e Pilhas",
                "Pilhas alcalinas, baterias de lítio, baterias de celular.",
                "Não perfurar ou incinerar. Embalar individualmente em saco plástico."
        );

        Categoria informatica = criarCategoria(
                "Informática",
                "Notebooks, desktops, teclados, mouses, cabos e periféricos.",
                "Remover dados pessoais antes do descarte. Realizar formatação."
        );

        Categoria celulares = criarCategoria(
                "Celulares e Tablets",
                "Smartphones, tablets, carregadores e acessórios.",
                "Realizar factory reset. Remover chip SIM e cartão de memória."
        );

        // --- Pontos de Coleta (usando "lgn" — coluna do seu SQL) ---
        criarPonto("Poupatempo Sé",
                "Praça do Carmo, s/n - Sé, São Paulo - SP",
                -23.5489, -46.6338, informatica);

        criarPonto("Shopping Ibirapuera - Ecoponto",
                "Av. Ibirapuera, 3103 - Moema, São Paulo - SP",
                -23.6097, -46.6648, baterias);

        criarPonto("FATEC Zona Sul",
                "Av. Paulistana, 1212 - Cidade Dutra, São Paulo - SP",
                -23.7301, -46.6978, celulares);

        criarPonto("Parque Estadual da Cantareira - Ecoponto",
                "Av. Deputado Emílio Carlos, 1831 - Tremembé, São Paulo - SP",
                -23.4100, -46.6542, informatica);

        System.out.println("✅ Dados iniciais carregados nas tabelas do projeto Sandra.");
    }

    private Categoria criarCategoria(String nome, String descricao, String instrucoes) {
        Categoria c = new Categoria();
        c.setNome(nome);
        c.setDescricao(descricao);
        c.setInstrucoesSeguranaca(instrucoes);
        return categoriaRepo.save(c);
    }

    // Parâmetro "lgn" para bater com o nome da coluna no banco
    private void criarPonto(String nome, String endereco,
                              double lat, double lgn, Categoria categoria) {
        PontoColeta p = new PontoColeta();
        p.setNome(nome);
        p.setEndereco(endereco);
        p.setLat(lat);
        p.setLgn(lgn);           // setLgn(), não setLng()
        p.setCategoria(categoria);
        pontoRepo.save(p);
    }
}
