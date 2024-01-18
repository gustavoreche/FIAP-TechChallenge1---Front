package com.br.fiap.cliente;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.fiap.integracao.BackendClient;
import com.br.fiap.utils.FormularioUtils;

@Service
public class ClienteFront {
	
	@Autowired
	private BackendClient client;
	
	@Autowired
	private FormularioUtils formularioUtils;
	
	private CadastroLeadDTO cadastroCliente;
	
	public void executa() {
		System.out.println(this.formularioInicioCliente());
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if(opcaoDigitada.equals("1")) {
				this.formularioUtils.cadastroLead();
				this.formularioUtils.formularioCadastroComSucesso();
				System.out.println(this.formularioInseridoNoSistema());
				repeteFormulario = false;
			} else if(opcaoDigitada.equals("2")) {
				//TODO: Verificar proposta
				System.out.println("""
						$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
						FEATURE NAO IMPLEMENTADA
						$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
						""");
				repeteFormulario = false;
			} else {
				System.out.println(this.formularioUtils.formularioOpcaoInvalida());
			}
		}
	}
	
	private String formularioInicioCliente() {
		return """
				------------------------------------------------------------------------
				CLIENTE, seja bem-vindo ao nosso SITE!!!!!!
				
				Digite qual opção você quer acessar:
					1 - Solicitar atendimento
					2 - Verificar proposta
				""";
	}
	
	private String formularioInseridoNoSistema() {
		return """
				Você foi inserido no sistema. Aguarde um de nossos vendedores entrar em contato. 
				Muito obrigado!!
				-------------------------------------------------------------------------------------
				""";
	}

}
