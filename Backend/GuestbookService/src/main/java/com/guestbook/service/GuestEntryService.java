package com.guestbook.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.guestbook.dto.EntryDto;
import com.guestbook.entity.Audit;
import com.guestbook.entity.Entry;
import com.guestbook.entity.UserDetails;
import com.guestbook.mapper.EntryMapper;
import com.guestbook.mapper.IBaseMapper;
import com.guestbook.repo.GuestEntryRepository;
import com.guestbook.repo.UserDetailsRepository;

import javassist.NotFoundException;

@Service
public class GuestEntryService extends BaseServiceImpl<EntryDto, Entry>{

	
	@Autowired
	private GuestEntryRepository guestEntryRepository;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Override
	public JpaRepository<Entry, Long> getJPARepository() {
		return this.guestEntryRepository;
	}

	@Override
	public IBaseMapper<EntryDto, Entry> getMapper() {
		return EntryMapper.INSTANCE;
	}
	
	public EntryDto create(EntryDto entryDto, String username) throws DataFormatException {
		if(entryDto == null || entryDto.getContent() == null)
			throw new DataFormatException("Data invalid");
		Entry entry = getMapper().convertToEntity(entryDto);
		updateAudit(entry, username);
		UserDetails user = userDetailsRepository.findByUsername(username);
		entry.setUser(user);
		entry = this.guestEntryRepository.saveAndFlush(entry);
		return getMapper().convertToDTO(entry);
	}
	
	public EntryDto update(EntryDto entryDto, String username) throws DataFormatException  {
		if(entryDto == null || entryDto.getId() < 0 || entryDto.getContent() == null)
			throw new DataFormatException("Data invalid"); 
		Entry entry = getMapper().convertToEntity(entryDto);
		updateAudit(entry, username);
		entry = this.guestEntryRepository.saveAndFlush(entry);
		return getMapper().convertToDTO(entry);
	}
	
	public EntryDto updateImage(MultipartFile multipartFile, String username, long entryID) throws IOException, NotFoundException {
		Optional<Entry> entry = this.getJPARepository().findById(entryID);
		if(!entry.isPresent()) 
			throw new NotFoundException("No data found");
		File file = null;
		file = convertMultiPartToFile(multipartFile);
        BufferedImage image = null;
        image = ImageIO.read(file);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        String url = fileStorageService.uploadImage(byteArray.toByteArray());
        Entry entryEntity = entry.get();
        entryEntity.setContent(url);
        entryEntity.setImage(true);
        updateAudit(entryEntity, username);
        entryEntity = this.guestEntryRepository.saveAndFlush(entryEntity);
        return getMapper().convertToDTO(entryEntity);
	}

	public EntryDto approveOrRejectEntry(long entryId, String username, boolean isApproved) throws NotFoundException {
		Optional<Entry> entry = this.getJPARepository().findById(entryId);
		if(!entry.isPresent()) 
			throw new NotFoundException("No data found");
		Entry entryEntity = entry.get();
		entryEntity.setApproved(isApproved);
		updateAudit(entryEntity, username);
        entryEntity = this.guestEntryRepository.saveAndFlush(entryEntity);
        return getMapper().convertToDTO(entryEntity);
	}
	
	public EntryDto deleteEntry(long entryId, String username) throws NotFoundException {
		Optional<Entry> entry = this.getJPARepository().findById(entryId);
		if(!entry.isPresent()) 
			throw new NotFoundException("No data found");
		Entry entryEntity = entry.get();
		entryEntity.setDeleted(true);
		updateAudit(entryEntity, username);
        entryEntity = this.guestEntryRepository.saveAndFlush(entryEntity);
        return getMapper().convertToDTO(entryEntity);
	}
	
	public EntryDto uploadImage(MultipartFile multipartFile, String username) throws IOException {
		File file = null;
		file = convertMultiPartToFile(multipartFile);
        BufferedImage image = null;
        image = ImageIO.read(file);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        String url = fileStorageService.uploadImage(byteArray.toByteArray());
        Entry entry = new Entry();
        entry.setContent(url);
        entry.setImage(true);
		UserDetails user = userDetailsRepository.findByUsername(username);
		entry.setUser(user);
        updateAudit(entry, username);
        entry = this.guestEntryRepository.saveAndFlush(entry);
        return getMapper().convertToDTO(entry);
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
	@Override
	public void updateAudit(Entry entity, String username) {
		if (entity != null) {
			Calendar cal = Calendar.getInstance();
			if (entity.getAudit() == null) {
				Audit audit = new Audit();
				audit.setCreatedUser(username);
				Date date = new Date(cal.getTime().getTime());
				audit.setCreatedDate(date);
				entity.setAudit(audit);
			} else {
				entity.getAudit().setUpdatedDate(new Date(cal.getTime().getTime()));
				entity.getAudit().setUpdatedUser(username);
			}
		}
	}
}
