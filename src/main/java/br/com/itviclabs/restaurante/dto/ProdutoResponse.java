package br.com.itviclabs.restaurante.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.itviclabs.restaurante.domain.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class ProdutoResponse {

	public ProdutoResponse (Produto produto) {
		this.nome = produto.getNome();
		this.preco = produto.getPreco();
	}
	
	private String nome;
	private BigDecimal preco;
}
