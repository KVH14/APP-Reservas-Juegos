package com.reservas.juegos.rawg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class RawgService {

    @Value("${rawg.api.key}")
    private String apiKey;

    @Value("${rawg.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<RawgJuegoDTO> buscar(String query) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiUrl + "/games")
                .queryParam("key", apiKey)
                .queryParam("search", query)
                .queryParam("page_size", 20)
                .toUriString();

        RawgRespuestaDTO respuesta = restTemplate.getForObject(url, RawgRespuestaDTO.class);
        return respuesta != null ? respuesta.getResults() : List.of();
    }

    public RawgJuegoDTO obtenerPorId(Long rawgId) {
        String url = UriComponentsBuilder
                .fromHttpUrl(apiUrl + "/games/" + rawgId)
                .queryParam("key", apiKey)
                .toUriString();

        return restTemplate.getForObject(url, RawgJuegoDTO.class);
    }
}
