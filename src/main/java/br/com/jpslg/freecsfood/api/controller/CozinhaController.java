package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.assembler.ApiMapper;
import br.com.jpslg.freecsfood.api.model.dto.CozinhaResponse;
import br.com.jpslg.freecsfood.api.model.dto.PageResponse;
import br.com.jpslg.freecsfood.api.model.dto.request.CozinhaRequest;
import br.com.jpslg.freecsfood.domain.model.Cozinha;
import br.com.jpslg.freecsfood.domain.service.CadastroCozinhaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {
    private final CadastroCozinhaService service;

    public CozinhaController(CadastroCozinhaService service) { this.service = service; }

    @GetMapping
    public PageResponse<CozinhaResponse> listar(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return PageResponse.from(service.listar(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public CozinhaResponse buscar(@PathVariable Long id) { return ApiMapper.toResponse(service.buscarOuFalhar(id)); }

    @PostMapping
    public ResponseEntity<CozinhaResponse> salvar(@RequestBody @Valid CozinhaRequest request) {
        Cozinha cozinha = new Cozinha();
        cozinha.setNome(request.nome());
        Cozinha salva = service.salvar(cozinha);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(ApiMapper.toResponse(salva));
    }

    @PutMapping("/{id}")
    public CozinhaResponse atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaRequest request) {
        Cozinha cozinha = service.buscarOuFalhar(id);
        cozinha.setNome(request.nome());
        return ApiMapper.toResponse(service.salvar(cozinha));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
