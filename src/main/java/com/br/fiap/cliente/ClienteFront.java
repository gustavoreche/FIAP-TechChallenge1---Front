package com.br.fiap.cliente;

import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class ClienteFront {
	
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
				this.filtrosDeBuscaDoVeiculo();
			} else if(opcaoDigitada.equals("2")) {
				this.filtrosDeBuscaDoVeiculo();
			} else {
				System.out.println("""
						!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						ATENÇAO!!!! Opção inválida, digite novamente!
						
						
						""");
			}
		}
	}
	
	private void filtrosDeBuscaDoVeiculo() {
		var entrada = new Scanner(System.in);
		System.out.println("""
				-----------------------------------------------------------
				Digite os filtros de busca do carro que você deseja:
				""");
		System.out.print("Digite o ANO do carro. Exemplo: 2020, 2016: ");
		var opcaoDigitada = entrada.nextLine();
		var ano = opcaoDigitada;
		System.out.print("Digite o MODELO do carro. Exemplo: Onix, Gol: ");
		opcaoDigitada = entrada.nextLine();
		var modelo = opcaoDigitada;
		System.out.print("Digite a CATEGORIA ano do carro. Exemplo: SUV, Sedan: ");
		opcaoDigitada = entrada.nextLine();
		var categoria = opcaoDigitada;
		var filtros = new FiltroDeBuscaDTO(
				ano, modelo, categoria
				);
		//validacao dos filtros no backend
	}

}
