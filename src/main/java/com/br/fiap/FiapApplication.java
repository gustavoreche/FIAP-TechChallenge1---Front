package com.br.fiap;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.br.fiap.cliente.ClienteFront;
import com.br.fiap.vendedor.VendedorFront;

@EnableFeignClients
@SpringBootApplication
public class FiapApplication implements CommandLineRunner {
	
	@Autowired
	private VendedorFront vendedorFront;
	
	@Autowired
	private ClienteFront clienteFront;

	public static void main(String[] args) {
		SpringApplication.run(FiapApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("""
				=============================================================
				Bem vindo ao sistema de CONSÓRCIO DE CARRO
				=============================================================
				""");
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.println(formularioClienteOuVendedor());
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if(opcaoDigitada.equals("1")) {
				this.clienteFront.executa();
			} else if(opcaoDigitada.equals("2")) {
				this.vendedorFront.executa();
			} else {
				System.out.println("""
						!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						ATENÇAO!!!! Opção inválida, digite novamente!
						
						
						""");
			}
		}
	}
	
	private String formularioClienteOuVendedor() {
		return """
				Digite a opção desejada para seguirmos a nossa simulação:
					1 - Se você é um CLIENTE
					2 - Se você é um VENDEDOR
				""";
	}

}
