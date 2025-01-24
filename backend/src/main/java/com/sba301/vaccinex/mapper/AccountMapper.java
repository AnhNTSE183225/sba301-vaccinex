package com.sba301.vaccinex.mapper;

import com.sba301.vaccinex.dto.AccountDTO;
import com.sba301.vaccinex.pojo.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "role.roleName", target = "roleName")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "nonLocked", target = "nonLocked")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "id", target = "id")
    AccountDTO accountToAccountDTO(Account account);

}
