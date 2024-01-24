package com.br.fiap.utils;

import org.springframework.stereotype.Component;

@Component
public class FormularioUtils {
	
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
	
	public void formularioCadastroComSucesso() {
		System.out.println("""
				------------------------------------------
				CADASTRO REALIZADO COM SUCESSO!!
				------------------------------------------
				""");
	}

}
