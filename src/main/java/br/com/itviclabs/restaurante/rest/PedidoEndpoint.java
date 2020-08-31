package br.com.itviclabs.restaurante.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.itviclabs.restaurante.domain.Pedido;
import br.com.itviclabs.restaurante.dto.ConfirmacaoPedido;
import br.com.itviclabs.restaurante.dto.PedidoRequest;
import br.com.itviclabs.restaurante.exception.ProdutosInvalidosException;
import br.com.itviclabs.restaurante.service.PedidoService;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/pedido")
@Slf4j
public class PedidoEndpoint {

	@Autowired
	private PedidoService pedidoService;
	
	@PostMapping
	public ConfirmacaoPedido efetuarPedido(@RequestBody PedidoRequest pedidoRequest) throws ProdutosInvalidosException {
		log.info("Authenticated user: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		log.info("efetuarPedido: {}", pedidoRequest);
		return pedidoService.efetuarPedido(pedidoRequest);
	}
	
	@GetMapping
	public Page<Pedido> findAll(@PageableDefault(direction = Direction.ASC) Pageable pageable){
		return pedidoService.findAll(pageable);
	}
	
}
