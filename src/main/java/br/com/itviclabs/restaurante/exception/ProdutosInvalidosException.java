package br.com.itviclabs.restaurante.exception;

import java.util.List;

import br.com.itviclabs.restaurante.dto.ProdutoRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProdutosInvalidosException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ProdutoRequest> produtosInvalidos;
	
	public ProdutosInvalidosException(List<ProdutoRequest> produtosInvalidos, String msg) {
		super(msg);
		this.produtosInvalidos = produtosInvalidos;
	}
}
