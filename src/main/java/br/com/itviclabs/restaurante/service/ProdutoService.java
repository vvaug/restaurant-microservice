package br.com.itviclabs.restaurante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itviclabs.restaurante.domain.Produto;
import br.com.itviclabs.restaurante.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public List<Produto> findAll(){
		log.info("Listando produtos disponiveis");
		return produtoRepository.findAll();
	}
}
