package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.assembler.ApiMapper;
import br.com.jpslg.freecsfood.api.model.dto.CidadeResponse;
import br.com.jpslg.freecsfood.api.model.dto.PageResponse;
import br.com.jpslg.freecsfood.api.model.dto.request.CidadeRequest;
import br.com.jpslg.freecsfood.domain.model.Cidade;
import br.com.jpslg.freecsfood.domain.model.Estado;
import br.com.jpslg.freecsfood.domain.service.CadastroCidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
    private final CadastroCidadeService service;

    public CidadeController(CadastroCidadeService service) { this.service = service; }

    @GetMapping
    public PageResponse<CidadeResponse> listar(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return PageResponse.from(service.listar(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public CidadeResponse buscar(@PathVariable Long id) { return ApiMapper.toResponse(service.buscarOuFalhar(id)); }

    @PostMapping
    public ResponseEntity<CidadeResponse> salvar(@RequestBody @Valid CidadeRequest request) {
        Cidade salva = service.salvar(novaCidade(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(ApiMapper.toResponse(salva));
    }

    @PutMapping("/{id}")
    public CidadeResponse atualizar(@PathVariable Long id, @RequestBody @Valid CidadeRequest request) {
        Cidade cidade = service.buscarOuFalhar(id);
        aplicar(request, cidade);
        return ApiMapper.toResponse(service.salvar(cidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Cidade novaCidade(CidadeRequest request) {
        Cidade cidade = new Cidade();
        aplicar(request, cidade);
        return cidade;
    }

    private void aplicar(CidadeRequest request, Cidade cidade) {
        cidade.setNome(request.nome());
        Estado estado = new Estado();
        estado.setId(request.estadoId());
        cidade.setEstado(estado);
    }
}
