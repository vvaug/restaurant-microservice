package br.com.itviclabs.restaurante.config;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.itviclabs.restaurante.domain.Pedido;
import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.repository.PedidoRepository;
import br.com.itviclabs.restaurante.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Setup implements CommandLineRunner{

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		log.info("Configurando base");
		
		List<Produto> produtos = 
				Arrays.asList(Produto.builder().id(1L).nome("Feijoada Completa").preco(new BigDecimal(20.00)).build()
							  );
		
		produtos.forEach(produto -> produtoRepository.save(produto));
		
		pedidoRepository.save(
				Pedido.builder()
					.produtos(produtos)
					.build());
		
		log.info("Configuracao de base finalizada");
	}

}
