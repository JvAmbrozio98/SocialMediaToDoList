package com.example.SocialMediaToDoList.Repository;

import com.example.SocialMediaToDoList.Models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts,Long> {
}
