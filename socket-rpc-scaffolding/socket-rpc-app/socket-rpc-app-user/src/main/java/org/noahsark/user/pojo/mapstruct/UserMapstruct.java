package org.noahsark.user.pojo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.noahsark.user.pojo.dto.UserDTO;
import org.noahsark.user.pojo.po.User;
import org.noahsark.user.pojo.vo.UserVO;


@Mapper(componentModel = "spring")
public interface UserMapstruct {
    UserMapstruct INSTANCE = Mappers.getMapper(UserMapstruct.class);

    User toPO(UserDTO userDTO);

    UserDTO toDTO(User user);

    UserVO toVO(User user);

}