package com.br.fiap.cliente;

public record ClienteNaFilaDTO (
		String nome,
		String email,
		String telefone,
		String anoFiltroDeBusca,
		String modeloFiltroDeBusca
) {

	public CadastroLeadDTO converteEmCadastro() {
		return new CadastroLeadDTO(
				nome, 
				telefone, 
				email, 
				anoFiltroDeBusca, 
				modeloFiltroDeBusca
				);
	}}
