package com.guestbook.mapper;


import java.io.Serializable;
import java.util.List;

import com.guestbook.dto.IDto;


public interface IBaseMapper<DTO extends IDto, Entity extends Serializable> {
	
	DTO convertToDTO(Entity entity);
	
	List<DTO> convertToDTOList(List<Entity> entities);

	Entity convertToEntity(DTO dto);

	List<Entity> convertToEntityList(List<DTO> dto);
}