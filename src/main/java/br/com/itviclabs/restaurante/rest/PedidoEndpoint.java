package br.com.itviclabs.restaurante.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.itviclabs.restaurante.dto.ConfirmacaoPedido;
import br.com.itviclabs.restaurante.dto.PedidoRequest;
import br.com.itviclabs.restaurante.exception.ProdutosInvalidosException;
import br.com.itviclabs.restaurante.service.PedidoService;

@CrossOrigin("*")
@RestController
@RequestMapping("/pedido")
public class PedidoEndpoint {

	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping
	public ConfirmacaoPedido efetuarPedido(@RequestBody PedidoRequest pedidoRequest) throws ProdutosInvalidosException {
		return pedidoService.efetuarPedido(pedidoRequest);
	}
	
}
