package com.br.fiap.vendedor;

import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.fiap.cliente.AtendimentoDTO;
import com.br.fiap.cliente.ClienteNaFilaDTO;
import com.br.fiap.integracao.BackendClient;

import feign.FeignException;

@Service
public class VendedorFront {
	
	@Autowired
	private BackendClient client;
	
	public void executa() {
		System.out.println("""
				==========================================================================================
				Bem vindo VENDEDOR!!
				
				Simulando que o VENDEDOR esteja acessando o CRM...
				
				==========================================================================================
				Estamos verificando se existe algum CLIENTE que acessou via SITE e foi para o CRM,
				para que você possa dar início ao atendimento do cliente...
				==========================================================================================
				""");
		ClienteNaFilaDTO proximoClienteNaFila = null;
		try {
			proximoClienteNaFila = this.client.proximoClienteDaFila();
		} catch (FeignException e) {
			System.out.println("""
					!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					CONSULTA NÃO REALIZADA
					
					Motivo: %s
					
					CONTATE UM ADMINISTRADOR DO SISTEMA
					!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							""".formatted(e.getMessage()));
		}
		if(Objects.isNull(proximoClienteNaFila)) {
			System.out.println("""
					Por enquanto nenhum CLIENTE esta na fila...
					
					Fique atento ao atendimento no ESTANDE, verifique após 15 minutos!!
					=============================================================================
					""");
		} else {
			System.out.println("""
					CLIENTE encontrado...
					
					Dados do cliente: 
						nome: %s
						email: %s
					=============================================================================
					""".formatted(proximoClienteNaFila.nome(), proximoClienteNaFila.email()));
			System.out.println("""
					Digite abaixo se você deseja capturar esse LEAD e iniciar o ATENDIMENTO?
						1 - SIM
						2 - NÃO
					""");
			var repeteFormulario = true;
			while(repeteFormulario) {
				System.out.print("Digite: ");
				var entrada = new Scanner(System.in);
				var opcaoDigitada = entrada.nextLine();
				if(opcaoDigitada.equals("1")) {
					System.out.println("""
							-------------------------------------------------------
							ATENDIMENTO iniciado...
							
							Agora, REGISTRE o ATENDIMENTO no sistema, informando os seguintes dados:
							""");
					System.out.print("Digite seu NOME: ");
					var nome = entrada.nextLine();
					
					try {
						this.client.registraAtendimento(new AtendimentoDTO(nome, proximoClienteNaFila.converteEmCadastro()));
						System.out.println("""
								!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								ATENDIMENTO registrado!!
								!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								""");
					} catch (FeignException e) {
						System.out.println("""
								!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								REGISTRO DE ATENDIMENTO NÃO REALIZADO
								
								Motivo: %s
								
								CONTATE UM ADMINISTRADOR DO SISTEMA
								!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
										""".formatted(e.getMessage()));
					}
					repeteFormulario = false;
				} else if(opcaoDigitada.equals("2")) {
					System.out.println("""
							............................................................
							............................................................
							Tudo bem... Volte ao ESTANDE
							............................................................
							............................................................
							""");
					repeteFormulario = false;
				} else {
					System.out.println("""
							!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							ATENÇAO!!!! Opção inválida, digite novamente!
							
							
							""");
				}
			}
		}
		
	}

}
