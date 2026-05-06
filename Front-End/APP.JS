/**
 * E-lixo BH - Aplicação de Geolocalização para Descarte de Resíduos Eletrônicos
 */

class ELixoBH {
    constructor() {
        this.pontosColeta = [];
        this.mapa = null;
        this.inicializarElementos();
        this.carregarDados();
        this.configurarEventos();
    }

    inicializarElementos() {
        this.getLocationBtn = document.getElementById('getLocationBtn');
        this.statusMessage = document.getElementById('statusMessage');
        this.resultsSection = document.getElementById('resultsSection');
        this.addPointSection = document.getElementById('addPointSection');
        this.mapSection = document.getElementById('mapSection');
        this.resultsList = document.getElementById('resultsList');
        this.addPointBtn = document.getElementById('addPointBtn');
        this.addPointForm = document.getElementById('addPointForm');
        this.pointName = document.getElementById('pointName');
        this.pointCep = document.getElementById('pointCep');

        this.resultsSection.style.display = 'none';
        this.addPointSection.style.display = 'none';
        this.mapSection.style.display = 'none';
    }

    configurarEventos() {
        this.getLocationBtn.addEventListener('click', () => this.obterLocalizacao());
        this.addPointBtn.addEventListener('click', () => this.toggleFormulario());
        this.addPointForm.addEventListener('submit', (e) => this.adicionarPonto(e));
    }

    async carregarDados() {
        try {
            const response = await fetch('PONTOS.JSON');
            this.pontosColeta = await response.json();
        } catch (error) {
            console.error('Erro ao carregar pontos de coleta:', error);
            this.pontosColeta = this.getDadosFallback();
        }
    }

    obterLocalizacao() {
        if (navigator.geolocation) {
            this.statusMessage.innerHTML = "Obtendo sua localização...";
            this.statusMessage.className = 'status-message status-loading';
            navigator.geolocation.getCurrentPosition(
                (position) => this.inicializarMapa(position),
                (error) => this.mostrarErro(error)
            );
        } else {
            this.statusMessage.innerHTML = "Geolocalização não é suportada pelo seu navegador.";
            this.statusMessage.className = 'status-message status-error';
        }
    }

    inicializarMapa(position) {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;

        this.mapSection.style.display = 'block';

        if (!this.mapa) {
            this.mapa = L.map('map').setView([latitude, longitude], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(this.mapa);

            this.pointMarkerLayer = L.layerGroup().addTo(this.mapa);
        } else {
            this.mapa.setView([latitude, longitude], 13);
        }

        this.mapa.invalidateSize();

        L.marker([latitude, longitude]).addTo(this.mapa)
            .bindPopup('Você está aqui!')
            .openPopup();

        this.mostrarPontosNoMapa();
        this.exibirListaPontos();
        this.mostrarResultados();
        this.mostrarAdicionarPonto();
        this.statusMessage.innerHTML = 'Aqui estão todos os pontos.';
        this.statusMessage.className = 'status-message status-success';
    }

    mostrarPontosNoMapa() {
        if (!this.mapa || !this.pointMarkerLayer) {
            return;
        }

        this.pointMarkerLayer.clearLayers();

        this.pontosColeta.forEach((ponto) => {
            L.marker([ponto.latitude, ponto.longitude]).addTo(this.pointMarkerLayer)
                .bindPopup(`<strong>${ponto.nome}</strong><br>${ponto.endereco}`);
        });
    }

    exibirListaPontos() {
        // Exibe a lista de pontos na seção de resultados
        this.resultsList.innerHTML = this.pontosColeta
            .map((ponto, index) => `
                <li data-index="${index}">
                    <span class="point-item" data-index="${index}">${ponto.nome} - ${ponto.endereco}</span>
                    <button type="button" class="delete-point-btn" data-index="${index}">Excluir</button>
                </li>
            `)
            .join('');

        document.querySelectorAll('.results-list .point-item').forEach((item) => {
            item.addEventListener('click', (e) => this.centralizarNoPonto(e));
        });

        document.querySelectorAll('.results-list .delete-point-btn').forEach((button) => {
            button.addEventListener('click', (e) => {
                e.stopPropagation();
                const index = Number(e.target.getAttribute('data-index'));
                this.excluirPonto(index);
            });
        });
    }

    excluirPonto(index) {
        if (index < 0 || index >= this.pontosColeta.length) {
            return;
        }

        const pontoRemovido = this.pontosColeta.splice(index, 1)[0];
        this.exibirListaPontos();
        this.mostrarPontosNoMapa();
        this.statusMessage.innerHTML = `Ponto "${pontoRemovido.nome}" removido.`;
        this.statusMessage.className = 'status-message status-success';
    }

    mostrarResultados() {
        this.resultsSection.style.display = 'block';
    }

    mostrarAdicionarPonto() {
        this.addPointSection.style.display = 'block';
    }

    centralizarNoPonto(event) {
        const index = event.target.getAttribute('data-index');
        const ponto = this.pontosColeta[index];

        // Centraliza o mapa no ponto selecionado
        this.mapa.setView([ponto.latitude, ponto.longitude], 15);

        // Adiciona um marcador temporário no ponto selecionado
        L.popup()
            .setLatLng([ponto.latitude, ponto.longitude])
            .setContent(`<strong>${ponto.nome}</strong><br>${ponto.endereco}`)
            .openOn(this.mapa);
    }

    toggleFormulario() {
        this.addPointForm.style.display = this.addPointForm.style.display === 'none' ? 'block' : 'none';
    }

    async adicionarPonto(event) {
        event.preventDefault();

        const nome = this.pointName.value.trim();
        const cep = this.pointCep.value.trim().replace(/\D/g, '');

        if (!nome || !cep || cep.length !== 8) {
            alert('Por favor, informe um nome e um CEP válido de 8 dígitos.');
            return;
        }

        this.statusMessage.innerHTML = 'Buscando endereço pelo CEP...';
        this.statusMessage.className = 'status-message status-loading';

        try {
            const { endereco, latitude, longitude } = await this.buscarEnderecoPorCep(cep);
            const novoPonto = { nome, endereco, cep, latitude, longitude };

            this.pontosColeta.push(novoPonto);
            this.inicializarOuAtualizarMapa(latitude, longitude, `<strong>${nome}</strong><br>${endereco}`);

            this.exibirListaPontos();
            this.addPointForm.reset();
            this.addPointForm.style.display = 'none';
            this.statusMessage.innerHTML = 'Ponto de coleta adicionado com sucesso!';
        } catch (error) {
            alert('Não foi possível localizar o CEP. Verifique o CEP e tente novamente.');
            this.statusMessage.innerHTML = 'Erro ao localizar CEP.';
            this.statusMessage.className = 'status-message status-error';
            console.error(error);
        }
    }

    async buscarEnderecoPorCep(cep) {
        const viacepUrl = `https://viacep.com.br/ws/${cep}/json/`;
        const cepResponse = await fetch(viacepUrl);

        if (!cepResponse.ok) {
            throw new Error('Falha na consulta de CEP');
        }

        const cepData = await cepResponse.json();
        if (cepData.erro) {
            throw new Error('CEP não encontrado');
        }

        const enderecoFormatado = [cepData.logradouro, cepData.bairro, cepData.localidade, cepData.uf]
            .filter(Boolean)
            .join(', ');

        const endereco = enderecoFormatado || `CEP ${cep}`;
        const { latitude, longitude } = await this.geocodeEndereco(`${endereco}, Brasil`);

        return { endereco, latitude, longitude };
    }

    async geocodeEndereco(endereco) {
        const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(endereco)}&limit=1&countrycodes=br`;
        const response = await fetch(url, {
            headers: {
                'Accept-Language': 'pt-BR'
            }
        });

        if (!response.ok) {
            throw new Error('Falha na consulta de geocodificação');
        }

        const dados = await response.json();
        if (!dados || !dados[0]) {
            throw new Error('Endereço não encontrado');
        }

        return {
            latitude: parseFloat(dados[0].lat),
            longitude: parseFloat(dados[0].lon)
        };
    }

    inicializarOuAtualizarMapa(latitude, longitude, popupContent) {
        if (!this.mapa) {
            this.mapa = L.map('map').setView([latitude, longitude], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }).addTo(this.mapa);
        } else {
            this.mapa.setView([latitude, longitude], 13);
        }

        L.marker([latitude, longitude]).addTo(this.mapa)
            .bindPopup(popupContent)
            .openPopup();

        this.mostrarPontosNoMapa();
    }

    mostrarErro(error) {
        const mensagensErro = {
            [error.PERMISSION_DENIED]: "Usuário negou a solicitação de geolocalização.",
            [error.POSITION_UNAVAILABLE]: "As informações de localização não estão disponíveis.",
            [error.TIMEOUT]: "A solicitação para obter a localização expirou."
        };

        this.statusMessage.innerHTML = mensagensErro[error.code] || "Ocorreu um erro desconhecido.";
        this.statusMessage.className = 'status-message status-error';
    }

    getDadosFallback() {
        return [
            {
                nome: "Ponto de Entrega Voluntária - Savassi",
                endereco: "Praça da Savassi, Savassi, Belo Horizonte - MG",
                latitude: -19.9415,
                longitude: -43.9350
            },
            {
                nome: "Ecoponto Venda Nova",
                endereco: "R. Maria Borborema, 80 - Venda Nova, Belo Horizonte - MG",
                latitude: -19.9880,
                longitude: -43.9340
            }
        ];
    }
}


document.addEventListener('DOMContentLoaded', () => {
    new ELixoBH();
});