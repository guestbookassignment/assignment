package com.guestbook.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.guestbook.dto.IDto;
import com.guestbook.mapper.IBaseMapper;

import javassist.NotFoundException;


@Service
public abstract class BaseServiceImpl <T extends IDto, S extends Serializable> implements IBaseService<T,S> {

	@Override
	public T create(T dto, String username) throws DataFormatException{
		S entity = getMapper().convertToEntity(dto);
		updateAudit(entity, username);
		if(entity != null){
			entity = getJPARepository().saveAndFlush(entity);
		}
		return getMapper().convertToDTO(entity);
	}

	@Override
	public T update(T dto, String username) throws DataFormatException {
		//Auto-generated method stub - Do nothing
		return dto;
	}

	@Override
	public void delete(T dto) {
		//Auto-generated method stub - Do nothing
		
	}

	@Override
	public T read(Long id) throws NotFoundException {
		Optional<S> entity = getJPARepository().findById(id);
		if(!entity.isPresent())
			throw new NotFoundException("There is no such element");
		return getMapper().convertToDTO(entity.get());
	}


	@Override
	public List<T> readAll() throws NotFoundException {
		List<S> entities = getJPARepository().findAll();
		
		if(entities == null || entities.size() == 0) {
			throw new NotFoundException("There are no elements to display");
		}
		return getMapper().convertToDTOList(entities);
	}

	public abstract JpaRepository<S, Long> getJPARepository();
	

	public abstract IBaseMapper<T, S> getMapper();

	@Override
	public abstract void updateAudit(S entity, String username);
	
}