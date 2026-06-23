package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.assembler.ApiMapper;
import br.com.jpslg.freecsfood.api.model.dto.EstadoResponse;
import br.com.jpslg.freecsfood.api.model.dto.PageResponse;
import br.com.jpslg.freecsfood.api.model.dto.request.EstadoRequest;
import br.com.jpslg.freecsfood.domain.model.Estado;
import br.com.jpslg.freecsfood.domain.service.CadastroEstadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/estados")
public class EstadoController {
    private final CadastroEstadoService service;

    public EstadoController(CadastroEstadoService service) { this.service = service; }

    @GetMapping
    public PageResponse<EstadoResponse> listar(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return PageResponse.from(service.listar(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public EstadoResponse buscar(@PathVariable Long id) { return ApiMapper.toResponse(service.buscarOuFalhar(id)); }

    @PostMapping
    public ResponseEntity<EstadoResponse> salvar(@RequestBody @Valid EstadoRequest request) {
        Estado estado = new Estado();
        estado.setNome(request.nome());
        Estado salvo = service.salvar(estado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(ApiMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public EstadoResponse atualizar(@PathVariable Long id, @RequestBody @Valid EstadoRequest request) {
        Estado estado = service.buscarOuFalhar(id);
        estado.setNome(request.nome());
        return ApiMapper.toResponse(service.salvar(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
