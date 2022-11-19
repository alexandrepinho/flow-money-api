package com.flowmoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flowmoney.api.dto.IdentityDTO;

@Entity
@Table(name = "fatura")
public class Fatura extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valorTotal;
	private boolean pago;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;
	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;
	@ManyToOne
	@JoinColumn(name = "usuario")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name = "cartao_credito")
	private CartaoCredito cartaoCredito;

	@OneToMany(mappedBy = "fatura", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<LancamentoFatura> lancamentos = new ArrayList<>();

	public Fatura(IdentityDTO fatura) {
		this.id = fatura.getId();
	}

	public Fatura(boolean pago, BigDecimal valorTotal, IdentityDTO cartaoCredito,
			LocalDate dataPagamento, LocalDate dataVencimento) {
		this.pago = pago;
		this.valorTotal = valorTotal;
		this.cartaoCredito = new CartaoCredito(cartaoCredito.getId());
		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVencimento;
	}

	public Fatura() {

	}

	public Fatura(Long id, LocalDate dataPagamento, boolean pago, BigDecimal valorTotal,
			IdentityDTO cartaoCredito, LocalDate dataVencimento) {
		this(pago, valorTotal, cartaoCredito, dataPagamento, dataVencimento);
		this.id = id;
	}

	public Fatura(Long id) {
		this.id = id;
	}

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public CartaoCredito getCartaoCredito() {
		return cartaoCredito;
	}

	public void setCartaoCredito(CartaoCredito cartaoCredito) {
		this.cartaoCredito = cartaoCredito;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

}
