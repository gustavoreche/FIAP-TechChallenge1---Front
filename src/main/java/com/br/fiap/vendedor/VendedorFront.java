package com.br.fiap.vendedor;

import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.LeadNaFilaDTO;
import com.br.fiap.integracao.BackendClient;
import com.br.fiap.utils.FormularioUtils;

import feign.FeignException;

@Service
public class VendedorFront {
	
	@Autowired
	private BackendClient client;
	
	@Autowired
	private FormularioUtils formularioUtils;
	
	public void executa() {
		System.out.println(this.formularioInicioVendedor());
		this.realizaAtendimento();
	}

	private void realizaAtendimento() {
		System.out.println(this.formularioInicioAtendimentoSite());
		this.formularioUtils.tempoDeEspera();
		LeadNaFilaDTO proximoLeadNaFila = null;
		try {
			proximoLeadNaFila = this.client.proximoLeadDaFila();
		} catch (FeignException e) {
			System.out.println(this.formularioConsultaNaoRealizada(e.getMessage()));
		}
		if(Objects.isNull(proximoLeadNaFila)) {
			System.out.println(this.formularioSemLeadNaFila());
		} else {
			System.out.println(this.formularioLeadNaFila(proximoLeadNaFila.nome()));
			System.out.println(this.formularioDesejaIniciarAtendimento());
			var repeteFormulario = true;
			while(repeteFormulario) {
				System.out.print("Digite: ");
				var entrada = new Scanner(System.in);
				var opcaoDigitada = entrada.nextLine();
				if(opcaoDigitada.equals("1")) {
					System.out.println(this.formularioAtendimentoIniciado());
					System.out.print("Digite seu NOME: ");
					var nome = entrada.nextLine();
					
					try {
						this.client.registraAtendimento(new AtendimentoDTO(nome, proximoLeadNaFila.converteEmCadastro()));
						System.out.println(this.formularioAtendimentoRegistrado());
					} catch (FeignException e) {
						System.out.println(formularioAtendimentoNaoRegistrado(e.getMessage()));
					}
					repeteFormulario = false;
				} else if(opcaoDigitada.equals("2")) {
					System.out.println(formularioVolteMaisTarde());
					repeteFormulario = false;
				} else {
					System.out.println(this.formularioUtils.formularioOpcaoInvalida());
				}
			}
		}
		
	}
	
	private String formularioInicioVendedor() {
		return """
				------------------------------------------------------------------------
				VENDEDOR, bem-vindo ao SITE
				------------------------------------------------------------------------
				""";
	}
	
	private String formularioInicioAtendimentoSite() {
		return """
				==========================================================================================
				
				Simulando que o VENDEDOR esteja acessando o atendimento pelo SITE...
				
				==========================================================================================
				Estamos verificando se existe algum LEAD que acessou via SITE,
				para que você possa dar início ao atendimento do lead...
				==========================================================================================
				""";
	}
	
	private String formularioConsultaNaoRealizada(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CONSULTA NÃO REALIZADA
				
				Motivo: %s
				
				CONTATE UM ADMINISTRADOR DO SISTEMA
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}
	
	private String formularioSemLeadNaFila() {
		return """
				Por enquanto nenhum LEAD esta na fila...
				
				Verifique após 15 minutos!!
				=============================================================================
				""";
	}
	
	private String formularioLeadNaFila(String nome) {
		return """
				LEAD encontrado...
				
				Dados do lead: 
					nome: %s
				=============================================================================
				""".formatted(nome);
	}
	
	private String formularioDesejaIniciarAtendimento() {
		return """
				Digite abaixo se você deseja capturar esse LEAD e iniciar o ATENDIMENTO?
				1 - SIM
				2 - NÃO
			""";
	}
	
	private String formularioAtendimentoIniciado() {
		return """
				-------------------------------------------------------
				ATENDIMENTO iniciado...
				
				Agora, REGISTRE o ATENDIMENTO no sistema, informando os seguintes dados:
				""";
	}
	
	private String formularioAtendimentoRegistrado() {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				ATENDIMENTO registrado!!
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				""";
	}
	
	private String formularioAtendimentoNaoRegistrado(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				REGISTRO DE ATENDIMENTO NÃO REALIZADO
				
				Motivo: %s
				
				CONTATE UM ADMINISTRADOR DO SISTEMA
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}
	
	private String formularioVolteMaisTarde() {
		return """
				............................................................
				............................................................
				Tudo bem... Volte mais tarde
				............................................................
				............................................................
				""";
	}
	
}
