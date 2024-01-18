package com.br.fiap.integracao;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.CadastroLeadDTO;
import com.br.fiap.cliente.LeadNaFilaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "backend", url = "http://localhost:8080")
public interface BackendClient {
	
	@PostMapping(value = "/lead")
    void cadastraLead(CadastroLeadDTO request);
	
	@GetMapping(value = "/lead/proximo-da-fila")
	LeadNaFilaDTO proximoLeadDaFila();
	
	@PostMapping(value = "/atendimento")
	void registraAtendimento(AtendimentoDTO request);

}
