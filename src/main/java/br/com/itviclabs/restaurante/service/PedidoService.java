package br.com.itviclabs.restaurante.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.itviclabs.restaurante.client.EntregadorClient;
import br.com.itviclabs.restaurante.domain.Pedido;
import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.dto.ConfirmacaoPedido;
import br.com.itviclabs.restaurante.dto.ConfirmacaoPedidoResponse;
import br.com.itviclabs.restaurante.dto.PedidoRequest;
import br.com.itviclabs.restaurante.dto.ProdutoRequest;
import br.com.itviclabs.restaurante.dto.ProdutoResponse;
import br.com.itviclabs.restaurante.exception.ProdutosInvalidosException;
import br.com.itviclabs.restaurante.repository.PedidoRepository;
import br.com.itviclabs.restaurante.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

	@Autowired
	EntregadorClient entregadorClient;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	
	public ConfirmacaoPedido efetuarPedido(PedidoRequest pedidoRequest) throws ProdutosInvalidosException {	
		
		log.info("Efetuando Pedido");
		
		Pedido pedido = buildPedido(pedidoRequest);
		
		pedidoRepository.save(pedido);
		
		if (pedidoRequest.getEntrega()) {
			log.info("Pedido para entrega");
			return enviarParaEntrega(pedidoRequest);
		}
		
		log.info("Pedido para retirada");
		
		return ConfirmacaoPedido.builder()
					.entrega(false)
					.produtos(getProdutosById(getProdutosId(pedidoRequest.getProdutos())))
					.tempoDeEspera("40 minutos")
					.build();
	}

	protected Pedido buildPedido(PedidoRequest pedidoRequest) throws ProdutosInvalidosException {
		
		List<ProdutoRequest> produtosInvalidos = new ArrayList<>();
		List<Produto> produtos = new ArrayList<>();
		
		pedidoRequest.getProdutos()
					.stream()
					.forEach(produto -> {
							
						Optional<Produto> exists = produtoRepository.findById(produto.getId());
							
						if (exists.isPresent()) {
							produtos.add(exists.get());
						}
						else {
							produtosInvalidos.add(produto);
						}
 					});
		
		
		if (! produtosInvalidos.isEmpty()) {
			throw new ProdutosInvalidosException(produtosInvalidos, "Os produtos informados não existem!");
		}
		
		return Pedido.builder()
					.produtos(produtos)
					.valorTotal(getValorTotal(produtos))
					.build();
					
	}
	
	protected BigDecimal getValorTotal(List<Produto> produtos) {
		
		BigDecimal valorTotal = BigDecimal.ZERO;
		
		for (Produto p : produtos) {
			valorTotal = valorTotal.add(p.getPreco());
		}
		
		return valorTotal;
	}
	
	protected List<ProdutoResponse> getProdutosById(List<Long> ids){
		return ids.stream()
				.map(id -> new ProdutoResponse(produtoRepository.findById(id).get()))
				.collect(Collectors.toList());
	}
	
	protected List<Long> getProdutosId(List<ProdutoRequest> produtos) {
		return produtos.stream()
					.map(produto -> produto.getId())
					.collect(Collectors.toList());
	}
	
	@HystrixCommand(fallbackMethod = "entregaFallback")
	protected ConfirmacaoPedido enviarParaEntrega(PedidoRequest pedidoRequest) {
		log.info("Realizando solicitação de entrega");
		ConfirmacaoPedidoResponse response = entregadorClient.realizarEntrega(pedidoRequest);
		return ConfirmacaoPedido.builder()
					.entrega(true)
					.produtos(getProdutosById(getProdutosId(response.getProdutos())))
					.tempoDeEspera(response.getTempoDeEspera())
					.build();
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected ConfirmacaoPedido entregaFallBack() {
		return ConfirmacaoPedido.builder()
					.entrega(null)
					.produtos(null)
					.tempoDeEspera(null)
					.retorno("Houve uma falha ao comunicar-se com o serviço de entrega")
					.build();
	}
	
}
