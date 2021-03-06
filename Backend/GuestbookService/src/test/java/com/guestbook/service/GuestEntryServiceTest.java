package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.guestbook.dto.EntryDto;
import com.guestbook.entity.Entry;
import com.guestbook.entity.UserDetails;
import com.guestbook.mapper.EntryMapper;
import com.guestbook.repo.GuestEntryRepository;
import com.guestbook.repo.UserDetailsRepository;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class GuestEntryServiceTest {
	@Mock
	private GuestEntryRepository guestEntryRepository;

	@Mock
	private UserDetailsRepository userDetailsRepository;

	@Mock
	private FileStorageService fileStorageService;

	@InjectMocks
	private GuestEntryService service;

	@Test
	public void test_create_success() throws NoSuchFieldException, SecurityException, Exception {
		EntryMapper instance = mock(EntryMapper.class);
		setFinalStatic(EntryMapper.class.getField("INSTANCE"), instance);
		EntryDto entryDto = new EntryDto();
		entryDto.setContent("test");
		Entry entry = new Entry();
		when(instance.convertToEntity(entryDto)).thenReturn(entry);
		String username = "test";
		UserDetails user = new UserDetails();
		when(userDetailsRepository.findByUsername(username)).thenReturn(user);
		when(instance.convertToDTO(entry)).thenReturn(entryDto);
		when(this.guestEntryRepository.saveAndFlush(entry)).thenReturn(entry);
		assertEquals(this.service.create(entryDto, username), entryDto);
	}

	@Test(expected = DataFormatException.class)
	public void test_create_Exception_ForNullInput() throws DataFormatException {
		this.service.create(null, "test");
	}
	
	@Test(expected = DataFormatException.class)
	public void test_create_Exception_ForInvalidInput() throws DataFormatException {
		this.service.create(new EntryDto(), "test");
	}
	
	@Test
	public void test_update() throws NoSuchFieldException, SecurityException, Exception {
		EntryMapper instance = mock(EntryMapper.class);
		setFinalStatic(EntryMapper.class.getField("INSTANCE"), instance);
		EntryDto entryDto = new EntryDto();
		entryDto.setContent("test");
		Entry entry = new Entry();
		when(instance.convertToEntity(entryDto)).thenReturn(entry);
		String username = "test";
		when(this.guestEntryRepository.saveAndFlush(entry)).thenReturn(entry);
		when(instance.convertToDTO(entry)).thenReturn(entryDto);
		assertEquals(this.service.update(entryDto, username), entryDto);
	}
	

	@Test(expected = DataFormatException.class)
	public void test_update_Exception_ForNullInput() throws DataFormatException {
		this.service.update(null, "test");
	}
	
	@Test(expected = DataFormatException.class)
	public void test_update_Exception_ForInvalidInput() throws DataFormatException {
		this.service.update(new EntryDto(), "test");
	}
	
	@Test
	public void test_approveOrRejectEntry() throws NoSuchFieldException, SecurityException, Exception {
		EntryMapper instance = mock(EntryMapper.class);
		setFinalStatic(EntryMapper.class.getField("INSTANCE"), instance);
		long entryId = 10l;
		EntryDto entryDto = new EntryDto();
		entryDto.setContent("test");
		Entry entry = new Entry();
		Optional<Entry> optionalEntry = Optional.of(entry);
		when(guestEntryRepository.findById(entryId)).thenReturn(optionalEntry);
		String username = "test";
		when(this.guestEntryRepository.saveAndFlush(entry)).thenReturn(entry);
		when(instance.convertToDTO(entry)).thenReturn(entryDto);
		assertEquals(this.service.approveOrRejectEntry(entryId, username,true), entryDto);
	}
	
	@Test(expected = NotFoundException.class)
	public void test_approveOrRejectEntry_Exception() throws NotFoundException {
		long entryId = 1l;
		Optional<Entry> entry = Optional.ofNullable(null);
		when(guestEntryRepository.findById(entryId)).thenReturn(entry);
		this.service.approveOrRejectEntry(entryId, "test",true);
	}
	

	@Test
	public void test_deleteEntry() throws NoSuchFieldException, SecurityException, Exception {
		EntryMapper instance = mock(EntryMapper.class);
		setFinalStatic(EntryMapper.class.getField("INSTANCE"), instance);
		long entryId = 10l;
		EntryDto entryDto = new EntryDto();
		entryDto.setContent("test");
		Entry entry = new Entry();
		Optional<Entry> optionalEntry = Optional.of(entry);
		when(guestEntryRepository.findById(entryId)).thenReturn(optionalEntry);
		String username = "test";
		when(this.guestEntryRepository.saveAndFlush(entry)).thenReturn(entry);
		when(instance.convertToDTO(entry)).thenReturn(entryDto);
		EntryDto result = this.service.deleteEntry(entryId, username);
		assertEquals(result, entryDto);

	}
	
	@Test(expected = NotFoundException.class)
	public void test_deleteEntry_Exception() throws NotFoundException {
		long entryId = 1l;
		Optional<Entry> entry = Optional.ofNullable(null);
		when(guestEntryRepository.findById(entryId)).thenReturn(entry);
		this.service.deleteEntry(entryId, "test");
	}

	private static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
}
