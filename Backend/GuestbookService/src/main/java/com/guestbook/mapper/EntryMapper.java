package com.guestbook.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.guestbook.dto.EntryDto;
import com.guestbook.entity.Entry;

@Mapper(uses = { UserDetailsMapper.class })
public abstract class EntryMapper implements IBaseMapper<EntryDto, Entry> {
	public static final EntryMapper INSTANCE = Mappers.getMapper(EntryMapper.class);

	@Mappings({ @Mapping(target = "user", ignore = true), @Mapping(target = "audit", ignore = true) })
	public abstract Entry convertToEntity(EntryDto dto);
}
