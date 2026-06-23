package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.assembler.ApiMapper;
import br.com.jpslg.freecsfood.api.model.dto.PageResponse;
import br.com.jpslg.freecsfood.api.model.dto.RestauranteResponse;
import br.com.jpslg.freecsfood.api.model.dto.request.RestauranteRequest;
import br.com.jpslg.freecsfood.domain.model.Cozinha;
import br.com.jpslg.freecsfood.domain.model.Restaurante;
import br.com.jpslg.freecsfood.domain.service.CadastroRestauranteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final CadastroRestauranteService service;

    public RestauranteController(CadastroRestauranteService service) { this.service = service; }

    @GetMapping
    public PageResponse<RestauranteResponse> listar(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return PageResponse.from(service.listar(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public RestauranteResponse buscar(@PathVariable Long id) {
        return ApiMapper.toResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    public ResponseEntity<RestauranteResponse> salvar(@RequestBody @Valid RestauranteRequest request) {
        Restaurante salvo = service.salvar(novoRestaurante(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(ApiMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public RestauranteResponse atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteRequest request) {
        Restaurante atual = service.buscarOuFalhar(id);
        aplicar(request, atual);
        return ApiMapper.toResponse(service.salvar(atual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Restaurante novoRestaurante(RestauranteRequest request) {
        Restaurante restaurante = new Restaurante();
        aplicar(request, restaurante);
        return restaurante;
    }

    private void aplicar(RestauranteRequest request, Restaurante restaurante) {
        restaurante.setNome(request.nome());
        restaurante.setTaxaFrete(request.taxaFrete());
        Cozinha cozinha = new Cozinha();
        cozinha.setId(request.cozinhaId());
        restaurante.setCozinha(cozinha);
    }
}
