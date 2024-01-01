package com.br.fiap.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.br.fiap.cliente.CadastroClienteDTO;

import feign.Headers;

@FeignClient(name = "backend", url = "http://localhost:8080")
public interface BackendClient {
	
	@PostMapping(value = "/cliente")
	@Headers("localAcessado: {localAcessado}")
    void cadastraCliente(CadastroClienteDTO request,
    		@RequestHeader("localAcessado") String localAcessado);

}
