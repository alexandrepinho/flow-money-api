package com.flowmoney.api.repository.transacao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.Transacao_;
import com.flowmoney.api.repository.filter.TransacaoFilter;

public class TransacaoRepositoryImpl implements TransacaoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Transacao> filtrar(TransacaoFilter transacaoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Transacao> criteria = builder.createQuery(Transacao.class);
		Root<Transacao> root = criteria.from(Transacao.class);

		Predicate[] predicates = criarRestricoes(transacaoFilter, builder, root);
		criteria.where(predicates);
		criteria.orderBy(builder.desc(root.get(Transacao_.data)));

		TypedQuery<Transacao> query = manager.createQuery(criteria);

		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(transacaoFilter));
	}

	private Predicate[] criarRestricoes(TransacaoFilter transacaoFilter, CriteriaBuilder builder,
			Root<Transacao> root) {

		List<Predicate> predicates = new ArrayList<>();

		predicates.add(builder.equal(root.get(Transacao_.usuario), transacaoFilter.getUsuario()));

		if (!ObjectUtils.isEmpty(transacaoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Transacao_.descricao)),
					"%" + transacaoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (transacaoFilter.getDataDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Transacao_.data), transacaoFilter.getDataDe()));
		}

		if (transacaoFilter.getDataAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Transacao_.data), transacaoFilter.getDataAte()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Transacao> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(TransacaoFilter transacaoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Transacao> root = criteria.from(Transacao.class);

		Predicate[] predicates = criarRestricoes(transacaoFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
