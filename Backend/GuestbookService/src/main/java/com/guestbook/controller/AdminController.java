package com.guestbook.controller;

import static com.guestbook.util.Constants.ADMIN_ENTRY_FILE_UPLOAD_URI;
import static com.guestbook.util.Constants.ADMIN_ENTRY_URI;
import static com.guestbook.util.Constants.APPROVE_ENTRY_URI;
import static com.guestbook.util.Constants.DELETE_ENTRY_URI;
import static com.guestbook.util.Constants.REJECT_ENTRY_URI;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.zip.DataFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.guestbook.dto.EntryDto;
import com.guestbook.service.GuestEntryService;

import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;

@RestController
public class AdminController {

	@Autowired
	private GuestEntryService guestEntryService;

	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@GetMapping(ADMIN_ENTRY_URI)
	public List<EntryDto> getAllEntries() throws NotFoundException {
		logger.info("AdminController: getAllEntries - Start");
		return this.guestEntryService.readAll();
	}

	@PutMapping(ADMIN_ENTRY_URI)
	public EntryDto updateEntry(
			@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization,
			@RequestBody EntryDto entryDto) throws DataFormatException {
		logger.info("AdminController: updateEntry - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("AdminController: updateEntry - Service Call, Username : " + username);
		return this.guestEntryService.update(entryDto, username);
	}

	@PutMapping(ADMIN_ENTRY_FILE_UPLOAD_URI)
	public EntryDto updateFile(
			@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization,
			@RequestParam("file") MultipartFile file, @PathVariable("id") long entryId) throws IOException, NotFoundException {
		logger.info("AdminController: updateFile - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("AdminController: updateFile - Service Call, Username : " + username);
		return this.guestEntryService.updateImage(file, username, entryId);
	}
	
	@PutMapping(APPROVE_ENTRY_URI)
	public EntryDto approveEntry(@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization, @PathVariable("id") long entryId) throws NotFoundException {
		logger.info("AdminController: approveEntry - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("AdminController: approveEntry - Service Call, Username : " + username);
		return this.guestEntryService.approveOrRejectEntry(entryId, username, true);
	}

	@PutMapping(REJECT_ENTRY_URI)
	public EntryDto rejectEntry(@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization, @PathVariable("id") long entryId) throws NotFoundException {
		logger.info("AdminController: rejectEntry - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("AdminController: rejectEntry - Service Call, Username : " + username);
		return this.guestEntryService.approveOrRejectEntry(entryId, username, false);
	}
	
	@DeleteMapping(DELETE_ENTRY_URI)
	public EntryDto deleteEntry(@ApiParam(value = "Authorization", required = false) @RequestHeader("Authorization") String authorization, @PathVariable("id") long entryId) throws NotFoundException {
		logger.info("AdminController: deleteEntry - Start");
		String authToken = authorization.substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		logger.info("AdminController: deleteEntry - Service Call, Username : " + username);
		return this.guestEntryService.deleteEntry(entryId, username);
	}
}
