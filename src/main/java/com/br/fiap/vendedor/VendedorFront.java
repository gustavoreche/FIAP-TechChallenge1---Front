package com.br.fiap.vendedor;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.LeadNaFilaDTO;
import com.br.fiap.cliente.ValorDaPropostaDTO;
import com.br.fiap.integracao.BackendClient;
import com.br.fiap.utils.FormularioUtils;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

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
						var atendimentoId = this.client.registraAtendimento(new AtendimentoDTO(nome, proximoLeadNaFila.converteEmCadastro()));
						System.out.println(this.formularioAtendimentoRegistrado(proximoLeadNaFila.modeloFiltroDeBusca(), proximoLeadNaFila.anoFiltroDeBusca()));
						System.out.println(this.formularioSimulandoConsultaDeValores());
						this.confirmaValorDaProposta(atendimentoId);
					} catch (FeignException e) {
						System.out.println(this.formularioAtendimentoNaoRegistrado(e.getMessage()));
					}
					repeteFormulario = false;
				} else if(opcaoDigitada.equals("2")) {
					System.out.println(this.formularioVolteMaisTarde());
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
				
				Dados do LEAD:
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
	
	private String formularioAtendimentoRegistrado(String modelo,
												   String ano) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				ATENDIMENTO registrado!!
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								
				Preferências do LEAD:
					modelo: %s
					ano: %s
				""".formatted(modelo, ano);
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

	private String formularioPropostaNaoEnviada(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				PROPOSTA NÃO ENVIADA
				
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

	private String formularioSimulandoConsultaDeValores() {
		return """
				............................................................
				
				Simulando que o VENDEDOR esteja consultando a TABELA DE VALORES NÃO DIGITALIZADA,
				baseada nos interesses do LEAD...
				
				............................................................
				""";
	}

	private void confirmaValorDaProposta(Long atendimentoId) {
		BigDecimal valorDaProposta = BigDecimal.valueOf(
				new Random().nextDouble() * 50_000
				).setScale(2, RoundingMode.HALF_UP);
		System.out.println(this.formularioConfirmaValorDaProposta(valorDaProposta));
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if (opcaoDigitada.equals("1")) {
				try {
					String respostaEnviaProposta = this.client.enviaProposta(atendimentoId, new ValorDaPropostaDTO(valorDaProposta));
					System.out.println(this.formularioPropostaEnviada(respostaEnviaProposta));
				} catch (FeignException e) {
					System.out.println(this.formularioPropostaNaoEnviada(e.getMessage()));
				}
				repeteFormulario = false;
			} else if (opcaoDigitada.equals("2")) {
				System.out.println(this.formularioVolteMaisTarde());
				repeteFormulario = false;
			} else {
				System.out.println(this.formularioUtils.formularioOpcaoInvalida());
			}
		}
	}

	private String formularioConfirmaValorDaProposta(BigDecimal valorDaProposta) {
		return """
    
				Digite abaixo se você deseja SALVAR o valor da PROPOSTA e ENVIAR a PROPOSTA ao LEAD:
				Valor da PROPOSTA: %s
					1 - SIM
					2 - NÃO
					""".formatted(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorDaProposta));
	}

	private String formularioPropostaEnviada(String respostaEnviaProposta) {
		return """
        !.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!
        
        PROPOSTA ENVIADA AO CLIENTE...
        %s
        
        !.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!.!
        """.formatted(respostaEnviaProposta);
	}

}
