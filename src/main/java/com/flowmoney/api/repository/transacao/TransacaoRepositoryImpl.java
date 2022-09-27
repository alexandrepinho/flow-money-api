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

import org.springframework.util.ObjectUtils;

import com.flowmoney.api.model.Transacao;
import com.flowmoney.api.model.Transacao_;
import com.flowmoney.api.repository.filter.TransacaoFilter;

public class TransacaoRepositoryImpl implements TransacaoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Transacao> filtrar(TransacaoFilter transacaoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Transacao> criteria = builder.createQuery(Transacao.class);
		Root<Transacao> root = criteria.from(Transacao.class);

		Predicate[] predicates = criarRestricoes(transacaoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Transacao> query = manager.createQuery(criteria);

		return query.getResultList();
	}

	private Predicate[] criarRestricoes(TransacaoFilter transacaoFilter, CriteriaBuilder builder,
			Root<Transacao> root) {

		List<Predicate> predicates = new ArrayList<>();

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

}
