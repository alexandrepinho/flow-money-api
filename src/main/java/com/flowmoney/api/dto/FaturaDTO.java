package com.flowmoney.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flowmoney.api.model.Fatura;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FaturaDTO {

	@NotNull(message = "Valor Total obrigatório")
	private BigDecimal valorTotal;
	private boolean pago;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	@NotNull(message = "Data de Vencimento obrigatória")
	@JsonFormat(pattern = "yyyy-M-dd")
	private LocalDate dataVencimento;
	@NotNull
	private IdentityDTO cartaoCredito;
	private boolean pagamentoParcial;
	private BigDecimal valorPago;

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
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

	public boolean isPagamentoParcial() {
		return pagamentoParcial;
	}

	public void setPagamentoParcial(boolean pagamentoParcial) {
		this.pagamentoParcial = pagamentoParcial;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Fatura transformarParaEntidade() {
		return new Fatura(this.pago, this.valorTotal, this.cartaoCredito, this.dataPagamento, this.dataVencimento, this.pagamentoParcial, this.valorPago);
	}

}
