package com.br.fiap.vendedor;

import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.CadastroClienteDTO;
import com.br.fiap.cliente.ClienteNaFilaDTO;
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
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if(opcaoDigitada.equals("1")) {
				this.atendimentoPeloSite();
				repeteFormulario = false;
			} else if(opcaoDigitada.equals("2")) {
				this.atendimentoPeloEstande();
				repeteFormulario = false;
			} else {
				System.out.println(this.formularioUtils.formularioOpcaoInvalida());
			}
		}
		
	}

	private void atendimentoPeloSite() {
		System.out.println(this.formularioInicioAtendimentoSite());
		this.formularioUtils.tempoDeEspera();
		ClienteNaFilaDTO proximoClienteNaFila = null;
		try {
			proximoClienteNaFila = this.client.proximoClienteDaFila();
		} catch (FeignException e) {
			System.out.println(this.formularioConsultaNaoRealizada(e.getMessage()));
		}
		if(Objects.isNull(proximoClienteNaFila)) {
			System.out.println(this.formularioSemClienteNaFila());
		} else {
			System.out.println(this.formularioClienteNaFila(proximoClienteNaFila.nome(), proximoClienteNaFila.email()));
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
						this.client.registraAtendimento(new AtendimentoDTO(nome, proximoClienteNaFila.converteEmCadastro()), "SITE");
						System.out.println(this.formularioAtendimentoRegistrado());
					} catch (FeignException e) {
						System.out.println(formularioAtendimentoNaoRegistrado(e.getMessage()));
					}
					repeteFormulario = false;
				} else if(opcaoDigitada.equals("2")) {
					System.out.println(formularioVolteAoEstande());
					repeteFormulario = false;
				} else {
					System.out.println(this.formularioUtils.formularioOpcaoInvalida());
				}
			}
		}
		
	}
	
	private void atendimentoPeloEstande() {
		var dadosCliente = this.formularioUtils.cadastroCliente("ESTANDE");
		this.formularioUtils.formularioCadastroComSucesso();
		this.registroDoAtendimento(dadosCliente);
		//TODO: Enviar proposta
	}
	
	private void registroDoAtendimento(CadastroClienteDTO dadosCliente) {
		var entrada = new Scanner(System.in);
		System.out.println(formularioRegistrandoAtendimento());
		try {
			this.client.registraAtendimento(new AtendimentoDTO("vendedor do ESTANDE", dadosCliente), "ESTANDE");
			System.out.println(this.formularioAtendimentoRegistrado());
		} catch (FeignException e) {
			System.out.println(this.formularioAtendimentoNaoRegistrado(e.getMessage()));
		}
		
	}
	
	private String formularioInicioVendedor() {
		return """
				------------------------------------------------------------------------
				VENDEDOR, digite qual operação você quer realizar:
					1 - Acesso ao SITE
					2 - Realizar atendimento no ESTANDE
				""";
	}
	
	private String formularioInicioAtendimentoSite() {
		return """
				==========================================================================================
				
				Simulando que o VENDEDOR esteja acessando o atendimento pelo SITE...
				
				==========================================================================================
				Estamos verificando se existe algum CLIENTE que acessou via SITE,
				para que você possa dar início ao atendimento do cliente...
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
	
	private String formularioSemClienteNaFila() {
		return """
				Por enquanto nenhum CLIENTE esta na fila...
				
				Fique atento ao atendimento no ESTANDE, verifique após 15 minutos!!
				=============================================================================
				""";
	}
	
	private String formularioClienteNaFila(String nome,
			String email) {
		return """
				CLIENTE encontrado...
				
				Dados do cliente: 
					nome: %s
					email: %s
				=============================================================================
				""".formatted(nome, email);
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
	
	private String formularioVolteAoEstande() {
		return """
				............................................................
				............................................................
				Tudo bem... Volte ao ESTANDE
				............................................................
				............................................................
				""";
	}
	
	private String formularioRegistrandoAtendimento() {
		return """
				Estamos registrando o ATENDIMENTO. Aguarde...
				-----------------------------------------------------------------------------------
				""";
	}
	
}
