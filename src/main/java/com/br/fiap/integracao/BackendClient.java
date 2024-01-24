package com.br.fiap.integracao;

import com.br.fiap.cliente.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "backend", url = "http://localhost:8080")
public interface BackendClient {
	
	@PostMapping(value = "/lead")
    void cadastraLead(CadastroLeadDTO request);
	
	@GetMapping(value = "/lead/proximo-da-fila")
	LeadNaFilaDTO proximoLeadDaFila();
	
	@PostMapping(value = "/atendimento")
	Long registraAtendimento(AtendimentoDTO request);

	@PutMapping(value = "/atendimento/envia-proposta/{atendimentoId}")
	String enviaProposta(@PathVariable(value = "atendimentoId") Long atendimentoId, ValorDaPropostaDTO request);

	@PutMapping(value = "/atendimento/recusa-proposta/{atendimentoId}")
	void recusaProposta(@PathVariable(value = "atendimentoId") Long atendimentoId);

	@GetMapping(value = "/atendimento/proposta/{nome}/{email}")
	InformaPropostaDTO pegaProposta(@PathVariable(value = "nome") String nome,
									@PathVariable(value = "email") String email);

	@PostMapping(value = "/contrato")
	void criaContrato(ContratoDTO request);
}
