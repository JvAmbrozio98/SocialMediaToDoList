package com.example.SocialMediaToDoList.Controllers;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;


public class MapperEntityToDTO {
    @Bean
    public ModelMapper modelMapper () {
        return new ModelMapper();
    };
}
