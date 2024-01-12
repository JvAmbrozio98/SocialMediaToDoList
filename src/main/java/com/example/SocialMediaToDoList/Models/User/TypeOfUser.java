package com.example.SocialMediaToDoList.Models.User;

public enum TypeOfUser {
    USER ("user"),
    ADMIN("admin"),
    DEV("dev");

     String role;

      TypeOfUser (String role) {
         this.role = role;
     }

     public String getRole () {
          return role;
     }

}
