package org.noahsark.online.pojo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.noahsark.online.pojo.po.SubjectOnline;
import org.noahsark.online.pojo.vo.SubjectOnlineVO;


@Mapper(componentModel = "spring")
public interface SubjectOnlineMapstruct {
    SubjectOnlineMapstruct INSTANCE = Mappers.getMapper(SubjectOnlineMapstruct.class);

    SubjectOnlineVO toVO(SubjectOnline subjectOnline);
}