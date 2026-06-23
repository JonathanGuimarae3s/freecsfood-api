package br.com.jpslg.freecsfood.domain.exception;

public class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;

    public EstadoNaoEncontradaException(String message) {
        super(message);
    }

    public EstadoNaoEncontradaException(Long id) {
        this("Não existe cadastro de ESTADO com código " + id);
    }
}
