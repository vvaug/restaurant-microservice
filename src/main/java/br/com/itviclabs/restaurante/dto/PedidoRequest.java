package br.com.itviclabs.restaurante.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class PedidoRequest {

	private Boolean entrega;
	private String nomeCliente;
	private String telefoneCliente;
	private String logradouro;
	private String numero;
	private List<ProdutoRequest> produtos;
	private String formaPagamento;
	
}
