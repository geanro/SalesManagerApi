package com.portfolio.salesmanager.mapper;


import com.portfolio.salesmanager.dto.response.UserResponse;
import com.portfolio.salesmanager.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        if (user==null) return null;

        return UserResponse.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .active(user.getActive())
                .branchId(user.getBranch() !=null ? user.getBranch().getIdBranch(): null)
                .branchName(user.getBranch() !=null ? user.getBranch().getName(): null)
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users){
        if (users==null) return List.of();

        return users.stream()
                .map(this::toResponse)
                .toList();
    }
}
