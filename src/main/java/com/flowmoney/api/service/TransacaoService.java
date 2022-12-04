package com.flowmoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.flowmoney.api.dto.TransacaoRelatorioMensalDTO;
import com.flowmoney.api.exceptionhandler.exception.CategoriaInexistenteException;
import com.flowmoney.api.exceptionhandler.exception.ContaInexistenteException;
import com.flowmoney.api.model.AbstractEntity;
import com.flowmoney.api.model.Categoria;
import com.flowmoney.api.model.Conta;
import com.flowmoney.api.model.Fatura;
import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.repository.CategoriaRepository;
import com.flowmoney.api.repository.ContaRepository;
import com.flowmoney.api.repository.FaturaRepository;
import com.flowmoney.api.repository.TransacaoRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class TransacaoService extends AbstractService<Transacao> {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private TransacaoRepository transacaoRepository;

	@Autowired
	private FaturaRepository faturaRepository;

	public byte[] relatorioPorPeriodo(LocalDate inicio, LocalDate fim) throws JRException {
		List<TransacaoRelatorioMensalDTO> dados = transacaoRepository.porPeriodo(inicio, fim);

		if (dados == null || dados.isEmpty()) {
			return new byte[0];
		}

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/transacoes-por-periodo.jasper");

		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public Transacao salvar(Transacao transacao) {

		verificarRegistrosAuxiliares(transacao);
		Conta conta = contaRepository.findById(transacao.getConta().getId()).orElse(null);
		if (conta == null) {
			throw new EmptyResultDataAccessException(1);
		}
		conta.atualizarSaldo(transacao);
		return transacaoRepository.save(transacao);

	}

	private void verificarRegistrosAuxiliares(Transacao transacao) {

		Categoria categoria = categoriaRepository.findById(transacao.getCategoria().getId()).orElse(null);

		if (categoria == null) {
			throw new CategoriaInexistenteException();
		}

		Conta conta = contaRepository.findById(transacao.getConta().getId()).orElse(null);

		if (conta == null) {
			throw new ContaInexistenteException();
		}
	}

	@Override
	public Transacao atualizar(Long id, Transacao transacao) {

		Transacao transacaoSalva = transacaoRepository.findById(id).orElse(null);

		if (transacaoSalva == null || transacaoSalva.getConta() == null) {
			throw new EmptyResultDataAccessException(1);
		}

		transacaoSalva.getConta().retirarEfeitoValorTransacao(transacaoSalva);
		if (!transacao.getConta().getId().equals(transacaoSalva.getConta().getId())) {
			contaRepository.save(transacaoSalva.getConta());
			transacao.setConta(contaRepository.findById(transacao.getConta().getId()).orElse(null));
		} else {
			transacao.setConta(transacaoSalva.getConta());
		}

		transacao.getConta().atualizarSaldo(transacao);
		contaRepository.save(transacao.getConta());

		BeanUtils.copyProperties(transacao, transacaoSalva, "id");
		return transacaoRepository.save(transacaoSalva);

	}

	public void removerTransacao(Long id) {
		Transacao transacao = transacaoRepository.findById(id).orElse(null);

		if (transacao == null) {
			throw new EmptyResultDataAccessException(1);
		}
		Fatura fatura = transacao.getFatura();

		if (fatura != null) {

			for (Transacao t : fatura.getTransacoes()) {
				Conta conta = transacao.getConta();
				conta.retirarEfeitoValorTransacao(t);
				contaRepository.save(conta);
			}

			transacaoRepository.deleteByIdIn(fatura.getTransacoes().stream().map(AbstractEntity::getId).toList());

			fatura.setPago(false);
			faturaRepository.save(fatura);
		} else {
			Conta conta = transacao.getConta();
			conta.retirarEfeitoValorTransacao(transacao);
			contaRepository.save(conta);
			transacaoRepository.deleteById(id);
		}
	}

}
