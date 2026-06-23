package br.com.jpslg.freecsfood.domain.exception;

public class RestauranteNaoEncontradaException extends EntidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;

    public RestauranteNaoEncontradaException(String message) {
        super(message);
    }

    public RestauranteNaoEncontradaException(Long id) {
        this("Não existe cadastro de RESTAURANTE com código " + id);
    }
}
