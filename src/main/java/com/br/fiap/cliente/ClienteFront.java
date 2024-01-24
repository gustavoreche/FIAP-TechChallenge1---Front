package com.br.fiap.cliente;

import com.br.fiap.integracao.BackendClient;
import com.br.fiap.utils.FormularioUtils;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

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
				this.cadastroLead();
				this.formularioUtils.formularioCadastroComSucesso();
				System.out.println(this.formularioInseridoNoSistema());
				repeteFormulario = false;
			} else if(opcaoDigitada.equals("2")) {
				this.buscaLead();
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
					2 - Simular que você recebeu o LINK da PROPOSTA da carta de crédito
				""";
	}
	
	private String formularioInseridoNoSistema() {
		return """
				Você foi inserido no sistema. Aguarde um de nossos vendedores entrar em contato. 
				Muito obrigado!!
				-------------------------------------------------------------------------------------
				""";
	}

	public void cadastroLead() {
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

	private String formularioContrato() {
		return """
				-----------------------------------------------------------
				Digite alguns dados para montarmos o CONTRATO:
				""";
	}

	public void buscaLead() {
		var entrada = new Scanner(System.in);

		var repeteFormulario = true;
		while (repeteFormulario) {
			System.out.println(this.formularioSimulaLoginDoLead());
			System.out.print("Digite seu NOME: ");
			var opcaoDigitada = entrada.nextLine();
			var nome = opcaoDigitada;
			System.out.print("Digite seu EMAIL: ");
			opcaoDigitada = entrada.nextLine();
			var email = opcaoDigitada;

			try {
				var valorDaProposta = this.client.pegaProposta(nome, email);
				if(Objects.isNull(valorDaProposta)) {
					System.out.println(this.formularioClienteNaoEncontrado());
				} else {
					this.aceiteOuRecusaDaProposta(nome, email, valorDaProposta);
				}
				repeteFormulario = false;
			} catch (FeignException e) {
				System.out.println(this.formularioBuscaNaoRealizadaContateAdministrador(e.getMessage()));
			}

		}

	}

	private void aceiteOuRecusaDaProposta(String nome,
										  String email,
										  InformaPropostaDTO valorDaProposta) {
		System.out.println(this.formularioExibeValorDaProposta(valorDaProposta.valorDaProposta()));
		System.out.println(this.formularioAceitaOuRecusaDaProposta());
		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.print("Digite: ");
			var entrada = new Scanner(System.in);
			var opcaoDigitada = entrada.nextLine();
			if (opcaoDigitada.equals("1")) {
				this.preencheContrato(nome, email, valorDaProposta);
				repeteFormulario = false;
			} else if (opcaoDigitada.equals("2")) {
				//TODO: RECUSOU
//				System.out.println(this.formularioVolteMaisTarde());
				repeteFormulario = false;
			} else {
				System.out.println(this.formularioUtils.formularioOpcaoInvalida());
			}
		}
	}

	private void preencheContrato(String nome,
								  String email,
								  InformaPropostaDTO valorDaProposta) {
		var entrada = new Scanner(System.in);

		var repeteFormulario = true;
		while(repeteFormulario) {
			System.out.println(this.formularioContrato());
			System.out.print("Digite seu CPF. Exemplo: 81528330021: ");
			var opcaoDigitada = entrada.nextLine();
			var cpf = opcaoDigitada;
			System.out.print("Digite o DIA DA DATA DE VENCIMENTO das parcelas. Exemplo: 5, 10: ");
			opcaoDigitada = entrada.nextLine();
			var diaDataVencimento = opcaoDigitada;

			this.formularioInformaParcelasEValores(valorDaProposta);
			System.out.print("Digite a QUANTIDADE DE PARCELAS que deseja pagar, e o valor da parcela vai ser preenchido automaticamente. Exemplo: 24 ou 36: ");
			opcaoDigitada = entrada.nextLine();
			var quantideDeParcelas = opcaoDigitada;
			ParcelasDTO parcelaEscolhida = valorDaProposta
					.parcelas()
					.stream()
					.filter(parcela -> quantideDeParcelas.equals(String.valueOf(parcela.quantidade())))
					.findFirst()
					.orElseGet(() -> new ParcelasDTO(-1, new BigDecimal(-1)));


			var contrato = new ContratoDTO(nome, email, cpf, diaDataVencimento, String.valueOf(parcelaEscolhida.quantidade()), String.valueOf(parcelaEscolhida.valor()));
			try {
				this.client.criaContrato(contrato);
				System.out.println(this.formularioContratoEnviado());
				repeteFormulario = false;
			} catch (FeignException e) {
				if(e.status() == HttpStatus.BAD_REQUEST.value()) {
					System.out.println(this.formularioContratoNaoRealizado(e.getMessage()));
				} else {
					System.out.println(this.formularioContratoNaoRealizadoContateAdministrador(e.getMessage()));
				}
			}

		}

	}

	private String formularioSimulaLoginDoLead() {
		return """
				-----------------------------------------------------------------------------------------------
				Para simularmos que o LEAD recebeu em seu e-mail, o link da PROPOSTA da carta de crédito,
				digite seu nome e seu e-mail:
				
				
				""";
	}

	private String formularioExibeValorDaProposta(BigDecimal valorDaProposta) {
		return """
    $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    
    O valor da PROPOSTA da venda da sua carta de crédito é: %s
    
    $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    """.formatted(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorDaProposta));
	}

	private String formularioAceitaOuRecusaDaProposta() {
		return """
    
				Digite abaixo se você deseja ACEITAR a PROPOSTA da compra da carta de crédito:
					1 - SIM
					2 - NÃO
					""";
	}

	private void formularioInformaParcelasEValores(InformaPropostaDTO valorDaProposta) {
		System.out.println("Opções de quantidade de parcelas e seu valor:");
		valorDaProposta
				.parcelas()
				.forEach(parcela -> System.out.println("""
    Quantidade de parcelas: %s
    Valor por parcela: %s
    """.formatted(parcela.quantidade(), parcela.valor())));
	}

	private String formularioBuscaNaoRealizadaContateAdministrador(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				BUSCA NÃO REALIZADA.
				
				Motivo: %s
				
				CONTATE UM ADMINISTRADOR DO SISTEMA
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}

	private String formularioClienteNaoEncontrado() {
		return """
    
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    CLIENTE NÃO ENCONTRADO
    
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    """;
	}

	private String formularioContratoNaoRealizado(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CONTRATO NÃO REALIZADO.
				
				Motivo: %s
				
				Tente novamente
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}

	private String formularioContratoNaoRealizadoContateAdministrador(String erro) {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CONTRATO NÃO REALIZADO.
				
				Motivo: %s
				
				CONTATE UM ADMINISTRADOR DO SISTEMA
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						""".formatted(erro);
	}

	private String formularioContratoEnviado() {
		return """
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				CONTRATO realizado!!
				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				""";
	}

}
