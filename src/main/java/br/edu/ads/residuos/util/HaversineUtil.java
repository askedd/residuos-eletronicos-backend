package br.edu.ads.residuos.util;

/**
 * Utilitário para cálculo de distância geoespacial.
 * Usa a Fórmula de Haversine, que considera a curvatura da Terra.
 */
public class HaversineUtil {

    // Raio médio da Terra em quilômetros
    private static final double RAIO_TERRA_KM = 6371.0;

    /**
     * Calcula a distância em km entre dois pontos geográficos.
     *
     * @param lat1 Latitude do ponto de origem
     * @param lng1 Longitude do ponto de origem
     * @param lat2 Latitude do destino
     * @param lng2 Longitude do destino
     * @return Distância em quilômetros (arredondada a 2 casas decimais)
     */
    public static double calcularDistancia(double lat1, double lng1,
                                           double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = RAIO_TERRA_KM * c;

        // Arredonda para 2 casas decimais
        return Math.round(distancia * 100.0) / 100.0;
    }

    // Construtor privado — classe utilitária, não deve ser instanciada
    private HaversineUtil() {}
}
