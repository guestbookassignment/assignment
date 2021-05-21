package com.guestbook.controller;

import static com.guestbook.util.Constants.ADMIN_ENTRY_URI;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guestbook.dto.EntryDto;
import com.guestbook.service.GuestEntryService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AdminController.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GuestEntryService guestEntryServiceMock;

	private static String ADMIN_ENTRY_FILE_UPLOAD_URI = "/admin/entry/1/file-upload";

	@Test
	public void test_getAllEntries_Success() throws Exception {
		List<EntryDto> entries = new ArrayList<>();
		when(guestEntryServiceMock.readAll()).thenReturn(entries);
		EntryDto dto = new EntryDto();
		dto.setApproved(true);
		dto.setContent("Test content");
		entries.add(dto);
		mockMvc.perform(get(ADMIN_ENTRY_URI).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].content", is("Test content")));
	}

	@Test
	public void test_getAllEntries_NoDataFound() throws Exception {
		when(guestEntryServiceMock.readAll()).thenThrow(new NotFoundException("No data"));
		mockMvc.perform(get(ADMIN_ENTRY_URI).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error-code", is("GB-001")))
				.andExpect(jsonPath("$.message", is("No data found")));
	}

	@Test
	public void test_updateEntry_success() throws Exception {
		EntryDto entryDto = new EntryDto();
		entryDto.setApproved(true);
		entryDto.setContent("Test content");
		String username = "user";
		when(guestEntryServiceMock.update(any(EntryDto.class), eq(username))).thenReturn(entryDto);
		mockMvc.perform(put(ADMIN_ENTRY_URI)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:password".getBytes()))
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(entryDto))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", is("Test content")));
	}

	@Test
	public void test_updateEntry_success_DataValidationFailure() throws Exception {
		EntryDto entryDto = new EntryDto();
		entryDto.setApproved(true);
		entryDto.setContent("Test content");
		String username = "user";
		when(guestEntryServiceMock.update(any(EntryDto.class), eq(username))).thenThrow(new DataFormatException());
		mockMvc.perform(put(ADMIN_ENTRY_URI)
				.header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:password".getBytes()))
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(entryDto)))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error-code", is("GB-003")))
				.andExpect(jsonPath("$.message", is("Internal server error")));
	}

	@Test
	public void test_updateFile_success() throws Exception {
		String username = "user";
		EntryDto entryDto = new EntryDto();
		entryDto.setApproved(true);
		entryDto.setContent("Test content");
		MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart(ADMIN_ENTRY_FILE_UPLOAD_URI);
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("PUT");
	            request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
	            return request;
	        }
	    });
	    long val =1l;
		when(guestEntryServiceMock.updateImage(any(MultipartFile.class), eq(username), eq(val))).thenReturn(entryDto);
		mockMvc.perform(builder.file("file", "Test Content".getBytes()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content", is("Test content")));
	}

	@Test
	public void test_updateFile_IOException() throws Exception {
		String username = "user";
	    long val =1l;
		MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart(ADMIN_ENTRY_FILE_UPLOAD_URI);
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("PUT");
	            request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
	            return request;
	        }
	    });
		when(guestEntryServiceMock.updateImage(any(MultipartFile.class), eq(username), eq(val))).thenThrow(new IOException("IO Error"));
		mockMvc.perform(builder.file("file", "Test Content".getBytes()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error-code", is("GB-002")))
				.andExpect(jsonPath("$.message", is("Internal server error")));
	}

	@Test
	public void test_updateFile_NoDataFound() throws Exception {
		String username = "user";
	    long val =1l;
		MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart(ADMIN_ENTRY_FILE_UPLOAD_URI);
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("PUT");
	            request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:password".getBytes()));
	            return request;
	        }
	    });
		when(guestEntryServiceMock.updateImage(any(MultipartFile.class), eq(username), eq(val))).thenThrow(new NotFoundException("No data"));
		mockMvc.perform(builder.file("file", "Test Content".getBytes()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error-code", is("GB-001")))
				.andExpect(jsonPath("$.message", is("No data found")));
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
