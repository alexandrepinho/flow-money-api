package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.model.Fatura;

public class FaturaResponseDTO {

	private Long id;
	@NotNull
	private BigDecimal valorTotal;
	private boolean pago;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;
	@NotNull
	private IdentityDTO conta;
	@NotNull
	private IdentityDTO cartaoCredito;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public IdentityDTO getConta() {
		return conta;
	}

	public void setConta(IdentityDTO conta) {
		this.conta = conta;
	}

	public IdentityDTO getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(IdentityDTO cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Fatura transformarParaEntidade() {

		return new Fatura(this.id, this.dataPagamento, this.pago, this.valorTotal, this.cartaoCredito, this.dataVencimento);
	}

}
