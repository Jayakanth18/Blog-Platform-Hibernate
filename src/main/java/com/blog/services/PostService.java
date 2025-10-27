package com.blog.services;

import java.util.List;
import com.blog.dao.PostDao;
import com.blog.entity.Post;
import com.blog.entity.User;

public class PostService {
    private PostDao postDao = new PostDao();

    public void createPost(String title, String content, User author) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        postDao.savePost(post);
    }

    public List<Post> getAllPosts() {
        return postDao.getAllPosts();
    }
    
    public Post getPostById(long id) {
        return postDao.getPostById(id);
    }

    public boolean deletePost(long postId, User currentUser) {
        Post post = postDao.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return false;
        }
        // Business logic: Only the author can delete their post
        if (post.getAuthor().getId() != currentUser.getId()) {
            System.out.println("You are not authorized to delete this post.");
            return false;
        }
        postDao.deletePost(post);
        return true;
    }
}