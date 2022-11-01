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

@Entity
@Table(name = "fatura")
public class Fatura extends AbstractEntity<Long> {

	@NotNull
	private BigDecimal valorTotal;
	private boolean pago;
	private LocalDate dataPagamento;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "conta")
	private Conta conta;
	
	@OneToMany(mappedBy = "fatura", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<LancamentoFatura> lancamentos = new ArrayList<>();

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

}
