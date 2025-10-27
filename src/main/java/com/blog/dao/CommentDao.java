package com.blog.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.blog.entity.Comment;
import com.blog.util.HibernateUtil;

public class CommentDao {

    public void saveComment(Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(comment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}