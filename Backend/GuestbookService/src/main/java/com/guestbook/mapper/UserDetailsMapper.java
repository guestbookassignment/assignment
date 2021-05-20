package com.guestbook.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.guestbook.dto.UserDto;
import com.guestbook.entity.UserDetails;

@Mapper
public abstract class UserDetailsMapper implements IBaseMapper<UserDto, UserDetails> {
	public static final UserDetailsMapper INSTANCE = Mappers.getMapper(UserDetailsMapper.class);

	@Mappings({ @Mapping(target = "role", ignore = true), @Mapping(target = "password", ignore = true) })
	public abstract UserDto convertToDTO(UserDetails entity);

	@Mappings(@Mapping(target = "role", ignore = true))
	public abstract UserDetails convertToEntity(UserDto dto);
}
