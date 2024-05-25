package com.devops.user_service.mappers;

import com.devops.user_service.dto.AddressDto;
import com.devops.user_service.dto.CreateUserRequest;
import com.devops.user_service.model.Address;
import com.devops.user_service.model.User;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {
    User createUserRequestToUser(CreateUserRequest createUserRequest);

    Address addressDtoToAddress(AddressDto addressDto);

}
