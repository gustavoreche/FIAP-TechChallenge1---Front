package com.br.fiap.cliente;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.br.fiap.integracao.BackendClient;

import feign.FeignException;

@Service
public class ClienteFront {
	
	@Autowired
	private BackendClient client;
	
	public void executa() {
		System.out.println("""
				------------------------------------------------------------------------
				Você quer acessar o sistema de consórcio de carro simulando por:
					1 - Acesso ao SITE
					2 - Visita ao ESTANDE
				""");
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if(opcaoDigitada.equals("1")) {
				this.filtrosDeBuscaDoVeiculo("site");
				this.mensagemCadastroComSucesso();
				System.out.println("""
						Você foi inserido no sistema. Aguarde um de nossos vendedores entrar em contato. 
						Muito obrigado!!
						""");
				System.exit(0);
			} else if(opcaoDigitada.equals("2")) {
				this.filtrosDeBuscaDoVeiculo("estande");
				this.mensagemCadastroComSucesso();
				repeteFormulario = false;
				//TODO: Realizar logica de solicitacao de valores
			} else {
				System.out.println("""
						!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						ATENÇAO!!!! Opção inválida, digite novamente!
						
						
						""");
			}
		}
	}
	
	private void filtrosDeBuscaDoVeiculo(String localAcessado) {
		var repeteFormulario = true;
		while(repeteFormulario) {
			var entrada = new Scanner(System.in);
			System.out.println("""
					-----------------------------------------------------------
					Digite os filtros de busca do carro que você deseja:
					""");
			System.out.print("Digite o ANO do carro ou aperte ENTER caso não seja prioridade. Exemplo: 2020, 2016: ");
			var opcaoDigitada = entrada.nextLine();
			var ano = opcaoDigitada;
			System.out.print("Digite o MODELO do carro ou aperte ENTER caso não seja prioridade. Exemplo: Onix, Gol: ");
			opcaoDigitada = entrada.nextLine();
			var modelo = opcaoDigitada;
			System.out.print("Digite a CATEGORIA ano do carro ou aperte ENTER caso não seja prioridade. Exemplo: SUV, Sedan: ");
			opcaoDigitada = entrada.nextLine();
			var categoria = opcaoDigitada;
			System.out.println("""
					Os filtros selecionados são:
						ano: %s
						modelo: %s
						categoria: %s
					""".formatted(ano, modelo, categoria));
			System.out.println("""
					-----------------------------------------------------------
					Agora preencha algumas informações sobre você:
					""");
			System.out.print("Digite seu NOME: ");
			opcaoDigitada = entrada.nextLine();
			var nome = opcaoDigitada;
			System.out.print("Digite seu TELEFONE. Exemplo: 16911223344: ");
			opcaoDigitada = entrada.nextLine();
			var telefone = opcaoDigitada;
			System.out.print("Digite seu EMAIL: ");
			opcaoDigitada = entrada.nextLine();
			var email = opcaoDigitada;
			var cadastroCliente = new CadastroClienteDTO(
					nome, telefone, email, ano, modelo, categoria
					);
			try {
				this.client.cadastraCliente(cadastroCliente, localAcessado.toUpperCase());
				repeteFormulario = false;
			} catch (FeignException e) {
				if(e.status() == HttpStatus.BAD_REQUEST.value()) {
					System.out.println("""
					!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					CADASTRO NÃO REALIZADO.
					
					Motivo: %s
					
					Tente novamente
					!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							""".formatted(e.getMessage()));
				}
			}
		}
		
	}
	
	private void mensagemCadastroComSucesso() {
		System.out.println("""
				------------------------------------------
				CADASTRO REALIZADO COM SUCESSO!!
				------------------------------------------
				""");
	}

}
