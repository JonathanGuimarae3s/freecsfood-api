package br.com.jpslg.freecsfood.api.controller;

import br.com.jpslg.freecsfood.api.assembler.ApiMapper;
import br.com.jpslg.freecsfood.api.model.dto.FormaPagamentoResponse;
import br.com.jpslg.freecsfood.api.model.dto.PageResponse;
import br.com.jpslg.freecsfood.api.model.dto.request.FormaPagamentoRequest;
import br.com.jpslg.freecsfood.domain.model.Estado;
import br.com.jpslg.freecsfood.domain.model.FormaPagamento;
import br.com.jpslg.freecsfood.domain.service.CadastroFormaPagamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private final CadastroFormaPagamentoService service;

    public FormaPagamentoController(CadastroFormaPagamentoService service) {
        this.service = service;
    }

    @GetMapping
    public PageResponse<FormaPagamentoResponse> listar(@PageableDefault(size = 20, sort = "descricao") Pageable pageable) {
        return PageResponse.from(service.listar(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public FormaPagamentoResponse buscar(@PathVariable Long id) {
        return ApiMapper.toResponse(service.buscarOuFalhar(id));
    }

    @PostMapping
    public ResponseEntity<FormaPagamentoResponse> salvar(@RequestBody @Valid FormaPagamentoRequest request) {
        FormaPagamento salva = service.salvar(novaFormaPagamento(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location).body(ApiMapper.toResponse(salva));
    }

    @PutMapping("/{id}")
    public FormaPagamentoResponse atualizar(@PathVariable Long id, @RequestBody @Valid FormaPagamentoRequest request) {
        FormaPagamento formaPagamento = service.buscarOuFalhar(id);
        aplicar(request, formaPagamento);
        return ApiMapper.toResponse(service.salvar(formaPagamento));
    }

    private FormaPagamento novaFormaPagamento(FormaPagamentoRequest request) {
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setDescricao(request.descricao());
        return formaPagamento;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private void aplicar(FormaPagamentoRequest request, FormaPagamento formaPagamento) {
        formaPagamento.setDescricao(request.descricao());
    }
}
