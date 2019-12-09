package br.com.caelum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.caelum.model.Produto;

public class TesteCache {
    public static void main(String[] args) {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfigurator.class);

        EntityManagerFactory emf = (EntityManagerFactory) ctx.getBean(EntityManagerFactory.class);        
        EntityManager em = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager(); // criando o segundo EntityManager
        
        Produto produto = em.find(Produto.class, 1);
        Produto outroProduto = em2.find(Produto.class, 1);
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Nome: " + outroProduto.getNome());
        
    }
}