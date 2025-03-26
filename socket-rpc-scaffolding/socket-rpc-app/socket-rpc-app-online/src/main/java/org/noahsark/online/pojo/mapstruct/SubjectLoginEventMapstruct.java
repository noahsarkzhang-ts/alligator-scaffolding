package org.noahsark.online.pojo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.noahsark.online.pojo.dto.SubjectLoginEventDTO;
import org.noahsark.online.pojo.po.SubjectLoginEvent;
import org.noahsark.online.pojo.vo.SubjectLoginEventVO;

@Mapper(componentModel = "spring")
public interface SubjectLoginEventMapstruct {
    SubjectLoginEventMapstruct INSTANCE = Mappers.getMapper(SubjectLoginEventMapstruct.class);

    SubjectLoginEvent toPO(SubjectLoginEventDTO subjectLoginEventDTO);

    SubjectLoginEventVO toVO(SubjectLoginEvent subjectLoginEvent);
}