package com.br.fiap.utils;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.br.fiap.cliente.CadastroClienteDTO;
import com.br.fiap.integracao.BackendClient;

import feign.FeignException;

@Component
public class FormularioUtils {
	
	@Autowired
	private BackendClient client;
	
	public void tempoDeEspera() {
		try {
			Thread.sleep(3000);
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
	
	public CadastroClienteDTO cadastroCliente(String localAcessado) {
		var entrada = new Scanner(System.in);
		System.out.println(formularioFiltrosDeBuscaDoCarro());
		System.out.print("Digite o ANO do carro ou aperte ENTER caso não seja prioridade. Exemplo: 2020, 2016: ");
		var opcaoDigitada = entrada.nextLine();
		var ano = opcaoDigitada;
		System.out.print("Digite o MODELO do carro ou aperte ENTER caso não seja prioridade. Exemplo: Onix, Gol: ");
		opcaoDigitada = entrada.nextLine();
		var modelo = opcaoDigitada;
		System.out.print("Digite a CATEGORIA ano do carro ou aperte ENTER caso não seja prioridade. Exemplo: SUV, Sedan: ");
		opcaoDigitada = entrada.nextLine();
		var categoria = opcaoDigitada;
		System.out.println(formularioFiltrosSelecionados(ano, modelo, categoria));
		
		CadastroClienteDTO cadastroCliente = null;
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.println(formularioPreenchaInformacoes());
			System.out.print("Digite seu NOME: ");
			opcaoDigitada = entrada.nextLine();
			var nome = opcaoDigitada;
			System.out.print("Digite seu TELEFONE. Exemplo: 16911223344: ");
			opcaoDigitada = entrada.nextLine();
			var telefone = opcaoDigitada;
			System.out.print("Digite seu EMAIL: ");
			opcaoDigitada = entrada.nextLine();
			var email = opcaoDigitada;
			cadastroCliente = new CadastroClienteDTO(
					nome, telefone, email, ano, modelo, categoria
					);
			try {
				this.client.cadastraCliente(cadastroCliente, localAcessado);
				repeteFormulario = false;
			} catch (FeignException e) {
				if(e.status() == HttpStatus.BAD_REQUEST.value()) {
					System.out.println(formularioCadastroNaoRealizado(e.getMessage()));
				} else {
					System.out.println(formularioCadastroNaoRealizadoContateAdministrador(e.getMessage()));
				}
			}
		
		}
		return cadastroCliente;
		
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
			String modelo,
			String categoria) {
		return """
				
				Os filtros selecionados são:
				ano: %s
				modelo: %s
				categoria: %s
			""".formatted(ano, modelo, categoria);
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
