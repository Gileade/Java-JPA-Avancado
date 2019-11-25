package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.caelum.model.Categoria;
import br.com.caelum.model.Loja;
import br.com.caelum.model.Produto;

@Repository
public class ProdutoDao {

	@PersistenceContext
	private EntityManager em;

	public List<Produto> getProdutos() {
		return em.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto getProduto(Integer id) {
		Produto produto = em.find(Produto.class, id);
		return produto;
	}

	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {
//		Código Original

//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//		CriteriaQuery<Produto> query = criteriaBuilder.createQuery(Produto.class);
//		Root<Produto> root = query.from(Produto.class);
//		
//		Path<String> nomePath = root.<String> get("nome");
//		Path<Integer> lojaPath = root.<Loja> get("loja").<Integer> get("id");
//		Path<Integer> categoriaPath = root.join("categorias").<Integer> get("id");
//
//		List<Predicate> predicates = new ArrayList<>();
//
//		if (!nome.isEmpty()) {
//			Predicate nomeIgual = criteriaBuilder.like(nomePath, nome);
//			predicates.add(nomeIgual);
//		}
//		if (categoriaId != null) {
//			Predicate categoriaIgual = criteriaBuilder.equal(categoriaPath, categoriaId);
//			predicates.add(categoriaIgual);
//		}
//		if (lojaId != null) {
//			Predicate lojaIgual = criteriaBuilder.equal(lojaPath, lojaId);
//			predicates.add(lojaIgual);
//		}
//
//		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
//
//		TypedQuery<Produto> typedQuery = em.createQuery(query);
//		return typedQuery.getResultList();

////////////////////////////////////////////////////////////////////////////////////////////////////
		// Desenvolvido somente com JPA

//String jpql = "select p from Produto p ";
//
//if(categoriaId != null){
//jpql += "join fetch p.categorias c";
//}
//
//if(nome != null || categoriaId != null || lojaId != null) {
//jpql += " where";
//
//if(!nome.isEmpty()) {    
//jpql += " p.nome like :pNome and";        
//}
//
//if(categoriaId != null) {    
//jpql += " c.id = :pCategoriaId and";        
//}
//
//if(lojaId != null) {    
//jpql += " p.loja.id = :pLojaId and";        
//}
//
//jpql += " 1=1";
//
//}
//
//TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
//
//if(!nome.isEmpty()) {    
//query.setParameter("pNome","%"+nome+"%");
//}
//
//if(categoriaId != null) {    
//query.setParameter("pCategoriaId", categoriaId);
//}
//
//if(lojaId != null) {    
//query.setParameter("pLojaId", lojaId);
//}
//
//return query.getResultList();
///////////////////////////////////////////////////////////////////////////////////////////////////////    

		// Desenvolvido com Criteria API
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Produto> query = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> produtoRoot = query.from(Produto.class);

		Predicate conjuncao = criteriaBuilder.conjunction();
		
		if (!nome.isEmpty()) {
			Path<String> nomeProduto = produtoRoot.<String> get("nome");
		    Predicate nomeIgual = criteriaBuilder.like(nomeProduto, "%" + nome + "%");

		    conjuncao = criteriaBuilder.and(nomeIgual);
		}
		if (categoriaId != null) {
			Join<Produto, List<Categoria>> join = produtoRoot.join("categorias");
		    Path<Integer> categoriaProduto = join.get("id");

		    conjuncao = criteriaBuilder.and(conjuncao,criteriaBuilder.equal(categoriaProduto, categoriaId));
		}
		if (lojaId != null) {
			Path<Loja> loja = produtoRoot.<Loja> get("loja");
		    Path<Integer> id = loja.<Integer> get("id");

		    conjuncao = criteriaBuilder.and(conjuncao, criteriaBuilder.equal(id, lojaId));		
		}

		TypedQuery<Produto> typedQuery = em.createQuery(query.where(conjuncao));
		return typedQuery.getResultList();
	}

	public void insere(Produto produto) {
		if (produto.getId() == null)
			em.persist(produto);
		else
			em.merge(produto);
	}

}
