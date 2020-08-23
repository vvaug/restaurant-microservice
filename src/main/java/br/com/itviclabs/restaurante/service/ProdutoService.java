package br.com.itviclabs.restaurante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Page<Produto> findAll(Pageable pageable){
		log.info("Listando produtos disponiveis");
		return produtoRepository.findAll(pageable);
	}

	public Produto findById(String id) {
		return produtoRepository.findById(Long.parseLong(id))
					.orElse(null);
	}
}
