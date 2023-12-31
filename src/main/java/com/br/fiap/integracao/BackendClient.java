package com.br.fiap.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.br.fiap.cliente.FiltroDeBuscaDTO;

@FeignClient("backend")
public interface BackendClient {
	
	@GetMapping(value = "/stores")
    Boolean isValidForm(FiltroDeBuscaDTO filtros);

}
