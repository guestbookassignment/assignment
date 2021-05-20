package com.guestbook.service;

import java.io.Serializable;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.guestbook.dto.IDto;

import javassist.NotFoundException;

@Service
@Transactional
public interface IBaseService<T extends IDto, S extends Serializable> {
	/**
	 * @param dto
	 * @return
	 */
	T create(T dto, String username) throws DataFormatException;	
	
	
	/**
	 * @param dto
	 * @return
	 */
	T update(T dto, String username);
	
	/**
	 * @param dto
	 */
	void delete(T dto);
	
	/**
	 * @param id
	 * @return
	 */
	T read(Long id)  throws NotFoundException;	
	
	/**
	 * @return
	 * @throws NotFoundException
	 */
	List<T> readAll() throws NotFoundException;
	
	
	/**
	 * @param entity
	 * @param username
	 */
	void updateAudit(S entity, String username);
}
