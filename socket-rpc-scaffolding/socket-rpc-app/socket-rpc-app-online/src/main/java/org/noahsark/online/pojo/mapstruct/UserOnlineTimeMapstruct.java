package org.noahsark.online.pojo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.noahsark.online.pojo.dto.UserOnlineTimeDTO;
import org.noahsark.online.pojo.po.UserOnlineTime;
import org.noahsark.online.pojo.vo.UserOnlineTimeVO;

@Mapper(componentModel = "spring")
public interface UserOnlineTimeMapstruct {
    UserOnlineTimeMapstruct INSTANCE = Mappers.getMapper(UserOnlineTimeMapstruct.class);

    UserOnlineTime toPO(UserOnlineTimeDTO userOnlineTimeDTO);

    UserOnlineTimeVO toVO(UserOnlineTime userOnlineTime);
}