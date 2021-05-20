package com.guestbook.controller;

import static com.guestbook.util.Constants.GUEST_ENTRY_URI;
import static com.guestbook.util.Constants.GUEST_ENTRY_FILE_UPLOAD_URI;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.DataFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.guestbook.dto.EntryDto;
import com.guestbook.service.GuestEntryService;

import io.swagger.annotations.ApiParam;

@RestController
public class GuestEntryController {

	@Autowired
	private GuestEntryService guestEntryService;
	
	private Logger logger = LoggerFactory.getLogger(GuestEntryController.class);
	
	@PostMapping(GUEST_ENTRY_URI)
	public EntryDto createEntry(@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization,
			@RequestBody EntryDto entryDto) throws DataFormatException {
		logger.info("GuestEntryController: createEntry - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("GuestEntryController: createEntry - Service Call, Username : " + username);
		return this.guestEntryService.create(entryDto, username);
	}
	
	@PostMapping(GUEST_ENTRY_FILE_UPLOAD_URI)
	public EntryDto uploadImage(@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization,
			@RequestParam("file") MultipartFile file) throws IOException {
		logger.info("GuestEntryController: uploadImage - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("GuestEntryController: uploadImage - Service Call, Username : " + username);
		return guestEntryService.uploadImage(file, username);
	}
}
