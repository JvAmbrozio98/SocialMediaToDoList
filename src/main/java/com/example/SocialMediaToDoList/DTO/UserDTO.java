package com.example.SocialMediaToDoList.DTO;

import com.example.SocialMediaToDoList.Models.User.User;
import com.fasterxml.jackson.annotation.JsonFilter;
import org.springframework.beans.BeanUtils;

@JsonFilter("DTOFilter")
public class UserDTO {
    private Long id;
    private String name;

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public  UserDTO(User user ) {
        BeanUtils.copyProperties(user,this);
    }
    public UserDTO() {}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
