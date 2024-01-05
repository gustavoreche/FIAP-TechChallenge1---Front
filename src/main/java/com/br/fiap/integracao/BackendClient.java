package com.br.fiap.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.CadastroClienteDTO;
import com.br.fiap.cliente.ClienteNaFilaDTO;

import feign.Headers;

@FeignClient(name = "backend", url = "http://localhost:8080")
public interface BackendClient {
	
	@PostMapping(value = "/cliente")
	@Headers("localAcessado: {localAcessado}")
    void cadastraCliente(CadastroClienteDTO request,
    		@RequestHeader("localAcessado") String localAcessado);
	
	@GetMapping(value = "/cliente/proximo-da-fila")
	ClienteNaFilaDTO proximoClienteDaFila();
	
	@PostMapping(value = "/vendedor/registra-atendimento")
	@Headers("localAcessado: {localAcessado}")
	void registraAtendimento(AtendimentoDTO request,
			@RequestHeader("localAcessado") String localAcessado);

}
