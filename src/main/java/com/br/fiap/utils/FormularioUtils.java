package com.br.fiap.utils;

import com.br.fiap.cliente.CadastroLeadDTO;
import com.br.fiap.integracao.BackendClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class FormularioUtils {
	
	@Autowired
	private BackendClient client;
	
	public void tempoDeEspera() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public String formularioOpcaoInvalida() {
		return """
		!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ATENÇAO!!!! Opção inválida, digite novamente!
		
		
		""";
	}
	
	public CadastroLeadDTO cadastroLead() {
		var entrada = new Scanner(System.in);

		CadastroLeadDTO cadastroLead = null;
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.println(this.formularioFiltrosDeBuscaDoCarro());
			System.out.print("Digite o ANO do carro. Exemplo: 2020, 2016: ");
			var opcaoDigitada = entrada.nextLine();
			var ano = opcaoDigitada;
			System.out.print("Digite o MODELO do carro. Exemplo: Onix, Gol: ");
			opcaoDigitada = entrada.nextLine();
			var modelo = opcaoDigitada;
			System.out.println(this.formularioFiltrosSelecionados(ano, modelo));

			System.out.println(this.formularioPreenchaInformacoes());
			System.out.print("Digite seu NOME: ");
			opcaoDigitada = entrada.nextLine();
			var nome = opcaoDigitada;
			System.out.print("Digite seu TELEFONE. Exemplo: 16911223344: ");
			opcaoDigitada = entrada.nextLine();
			var telefone = opcaoDigitada;
			System.out.print("Digite seu EMAIL: ");
			opcaoDigitada = entrada.nextLine();
			var email = opcaoDigitada;
			cadastroLead = new CadastroLeadDTO(
					nome, telefone, email, ano, modelo
					);
			try {
				this.client.cadastraLead(cadastroLead);
				repeteFormulario = false;
			} catch (FeignException e) {
				if(e.status() == HttpStatus.BAD_REQUEST.value()) {
					System.out.println(this.formularioCadastroNaoRealizado(e.getMessage()));
				} else {
					System.out.println(this.formularioCadastroNaoRealizadoContateAdministrador(e.getMessage()));
				}
			}
		
		}
		return cadastroLead;
		
	}
	
	public void formularioCadastroComSucesso() {
		System.out.println("""
				------------------------------------------
				CADASTRO REALIZADO COM SUCESSO!!
				------------------------------------------
				""");
	}
	
	private String formularioFiltrosDeBuscaDoCarro() {
		return """
				-----------------------------------------------------------
				Digite os filtros de busca do carro que você deseja:
				""";
	}
	
	private String formularioFiltrosSelecionados(String ano,
			String modelo) {
		return """
				
				Os filtros selecionados são:
				ano: %s
				modelo: %s
			""".formatted(ano, modelo);
	}
	
	private String formularioPreenchaInformacoes() {
		return """
				-----------------------------------------------------------
				Agora preencha algumas informações sobre você:
				""";
	}
	
	private String formularioCadastroNaoRealizado(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CADASTRO NÃO REALIZADO.
				
				Motivo: %s
				
				Tente novamente
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}
	
	private String formularioCadastroNaoRealizadoContateAdministrador(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CADASTRO NÃO REALIZADO.
				
				Motivo: %s
				
				CONTATE UM ADMINISTRADOR DO SISTEMA
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}

}
