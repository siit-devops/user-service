package com.devops.user_service.service.impl;

import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.mappers.MapStructMapper;
import com.devops.user_service.model.User;
import com.devops.user_service.repository.UserRepository;
import com.devops.user_service.service.KeycloakAdminClientService;
import com.devops.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminClientService keycloakService;
    private final MapStructMapper mapper;

    @Override
    @Transactional
    public void updateUser(UUID id, CreateUserRequest editUserRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BadRequestException("User doesn't exist")
        );

        if (editUserRequest.getId().compareTo(user.getId()) != 0) {
            throw new BadRequestException("Can't modify user's ID");
        }

        keycloakService.updateUser(editUserRequest);
        user = mapper.createUserRequestToUser(editUserRequest);

        userRepository.save(user);
    }

}
