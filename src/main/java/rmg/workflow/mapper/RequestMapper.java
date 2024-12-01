package rmg.workflow.mapper;

import org.mapstruct.Mapper;
import rmg.workflow.model.dto.RequestDto;
import rmg.workflow.model.entity.Requests;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDto toRequestDto(Requests entity);
}
