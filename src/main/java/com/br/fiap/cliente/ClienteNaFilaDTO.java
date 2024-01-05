package com.br.fiap.cliente;

public record ClienteNaFilaDTO (
		String nome,
		String email,
		String telefone,
		String anoFiltroDeBusca,
		String modeloFiltroDeBusca,
		String categoriaFiltroDeBusca
) {

	public CadastroClienteDTO converteEmCadastro() {
		return new CadastroClienteDTO(
				nome, 
				telefone, 
				email, 
				anoFiltroDeBusca, 
				modeloFiltroDeBusca, 
				categoriaFiltroDeBusca
				);
	}}
