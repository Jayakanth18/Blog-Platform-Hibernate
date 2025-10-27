package com.blog.services;

import com.blog.dao.CommentDao;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;

public class CommentService {
    private CommentDao commentDao = new CommentDao();

    public void addComment(String text, Post post, User author) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setPost(post);
        comment.setAuthor(author);
        commentDao.saveComment(comment);
    }
}