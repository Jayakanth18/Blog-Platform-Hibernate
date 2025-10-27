package com.blog;

import java.util.List;
import java.util.Scanner;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.services.CommentService;
import com.blog.services.PostService;
import com.blog.services.UserService;
import com.blog.util.HibernateUtil;

public class App {
    
    private static UserService userService = new UserService();
    private static PostService postService = new PostService();
    private static CommentService commentService = new CommentService();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        run();
        HibernateUtil.shutdown(); // Close Hibernate session factory
        scanner.close();
    }

    private static void run() {
        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- Blog Platform ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. View All Posts");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");
        int choice = getIntInput();

        switch (choice) {
            case 1: register(); break;
            case 2: login(); break;
            case 3: viewAllPosts(); break;
            case 4: System.out.println("Exiting..."); System.exit(0);
            default: System.out.println("Invalid option.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- Welcome, " + currentUser.getUsername() + " ---");
        System.out.println("1. Create New Post");
        System.out.println("2. View All Posts");
        System.out.println("3. Add Comment to a Post");
        System.out.println("4. Delete a Post");
        System.out.println("5. Logout");
        System.out.print("Choose option: ");
        int choice = getIntInput();

        switch (choice) {
            case 1: createPost(); break;
            case 2: viewAllPosts(); break;
            case 3: addComment(); break;
            case 4: deletePost(); break;
            case 5: logout(); break;
            default: System.out.println("Invalid option.");
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        if (userService.registerUser(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Try a different username.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        currentUser = userService.loginUser(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void createPost() {
        System.out.print("Enter post title: ");
        String title = scanner.nextLine();
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();
        
        postService.createPost(title, content, currentUser);
        System.out.println("Post created successfully!");
    }

    private static void viewAllPosts() {
        List<Post> posts = postService.getAllPosts();
        if (posts == null || posts.isEmpty()) {
            System.out.println("No posts found.");
            return;
        }

        System.out.println("\n--- All Posts ---");
        for (Post post : posts) {
            System.out.println("---------------------------------");
            System.out.println("Post ID: " + post.getId());
            System.out.println("Title: " + post.getTitle());
            System.out.println("Author: " + post.getAuthor().getUsername());
            System.out.println("Content: " + post.getContent());
            viewComments(post.getId(), false); // Don't fetch post again
            System.out.println("---------------------------------");
        }
    }

    private static void addComment() {
        System.out.print("Enter Post ID to comment on: ");
        long postId = getLongInput();
        
        Post post = postService.getPostById(postId);
        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        System.out.print("Enter your comment: ");
        String text = scanner.nextLine();
        commentService.addComment(text, post, currentUser);
        System.out.println("Comment added successfully!");
    }
    
    private static void deletePost() {
        System.out.print("Enter Post ID to delete: ");
        long postId = getLongInput();
        
        if (postService.deletePost(postId, currentUser)) {
            System.out.println("Post deleted successfully.");
        } else {
            System.out.println("Failed to delete post.");
        }
    }

    private static void viewComments(long postId, boolean fetchPost) {
        Post post;
        if (fetchPost) {
             post = postService.getPostById(postId);
             if (post == null) {
                 System.out.println("Post not found.");
                 return;
             }
        } else {
            // This is a simplified way for the viewAllPosts list
            // A more robust way would be to query comments separately
            post = postService.getPostById(postId); // Re-fetch to be sure
        }
        
        List<Comment> comments = post.getComments();
        if (comments.isEmpty()) {
            System.out.println("  [No comments yet]");
        } else {
            System.out.println("  Comments:");
            for (Comment comment : comments) {
                System.out.println("    - " + comment.getText() + " (by " + comment.getAuthor().getUsername() + ")");
            }
        }
    }

    private static void logout() {
        System.out.println("Logging out " + currentUser.getUsername());
        currentUser = null;
    }
    
    // Helper methods for safe input
    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private static long getLongInput() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}