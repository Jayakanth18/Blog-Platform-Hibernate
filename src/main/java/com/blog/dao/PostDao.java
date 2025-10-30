package com.blog.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.blog.entity.Post;
import com.blog.util.HibernateUtil;

public class PostDao {

    @SuppressWarnings("deprecation")
	public void savePost(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Post getPostById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use HQL to fetch the post and its comments/author eagerly
            Query<Post> query = session.createQuery(
                "SELECT p FROM Post p " +
                "LEFT JOIN FETCH p.author " +
                "LEFT JOIN FETCH p.comments c " +
                "LEFT JOIN FETCH c.author " +
                "WHERE p.id = :id", Post.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> getAllPosts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             // Fetch authors to avoid N+1 problem
            return session.createQuery("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.author ORDER BY p.id DESC", Post.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void deletePost(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}