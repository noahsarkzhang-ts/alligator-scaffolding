package org.noashark.app.user.pojo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.noashark.app.user.pojo.dto.UserDTO;
import org.noashark.app.user.pojo.po.User;
import org.noashark.app.user.pojo.vo.UserVO;


@Mapper(componentModel = "spring")
public interface UserMapstruct {
    UserMapstruct INSTANCE = Mappers.getMapper(UserMapstruct.class);

    User toPO(UserDTO userDTO);

    UserDTO toDTO(User user);

    UserVO toVO(User user);

}