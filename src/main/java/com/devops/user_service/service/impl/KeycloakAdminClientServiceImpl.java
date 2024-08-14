package com.devops.user_service.service.impl;

import com.devops.user_service.config.KeycloakProvider;
import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.mappers.MapStructMapper;
import com.devops.user_service.repository.UserRepository;
import com.devops.user_service.service.KeycloakAdminClientService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClientServiceImpl implements KeycloakAdminClientService {

    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider kcProvider;
    private final MapStructMapper mapstructMapper;
    private final UserRepository userRepository;


    @Override
    public Response createKeycloakUser(CreateUserRequest user) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(true);

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            addRoleToUser(userId, user.getRole(), usersResource);
            saveUserToDb(userId, user);
        }

        return response;
    }

    @Override
    public void updateUser(CreateUserRequest editUserRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(editUserRequest.getUsername());
        user.setFirstName(editUserRequest.getFirstname());
        user.setLastName(editUserRequest.getLastname());
        user.setEmail(editUserRequest.getEmail());

        if (editUserRequest.getPassword() != null) {
            CredentialRepresentation credential = createPasswordCredentials(editUserRequest.getPassword());
            user.setCredentials(Collections.singletonList(credential));
        }

        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        try {
            usersResource.get(editUserRequest.getId().toString()).update(user);
        } catch (ClientErrorException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    private void addRoleToUser(String userId, String role, UsersResource usersResource) {
        UserResource userResource = usersResource.get(userId);
        RolesResource rolesResource = kcProvider.getInstance().realm(realm).roles();
        RoleRepresentation roleRepresentation = rolesResource.get(role).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void saveUserToDb(String userId, CreateUserRequest user) {
        user.setId(UUID.fromString(userId));
        var localUser = mapstructMapper.createUserRequestToUser(user);
        userRepository.save(localUser);
    }
}
