package com.devops.user_service.service.impl;

import com.devops.user_service.dto.ChangePasswordRequest;
import com.devops.user_service.dto.EditUserRequest;
import com.devops.user_service.exception.BadRequestException;
import com.devops.user_service.exception.NotFoundException;
import com.devops.user_service.mappers.MapStructMapper;
import com.devops.user_service.model.User;
import com.devops.user_service.repository.UserRepository;
import com.devops.user_service.service.KeycloakAdminClientService;
import com.devops.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminClientService keycloakService;
    private final MapStructMapper mapper;

    @Override
    @Transactional
    public void updateUser(UUID id, EditUserRequest editUserRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User doesn't exist")
        );

        if (editUserRequest.getId().compareTo(user.getId()) != 0) {
            throw new BadRequestException("Can't modify user's ID");
        }

        keycloakService.updateUser(editUserRequest);
        user = mapper.editUserRequestToUser(editUserRequest);

        userRepository.save(user);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest changePasswordRequest) {
        keycloakService.changePassword(id, changePasswordRequest);
    }

}
