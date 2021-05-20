package com.guestbook.util;

public class Constants {
	private Constants() {}
	public static final String GUEST_ENTRY_URI = "/guest/entry";
	public static final String GUEST_ENTRY_FILE_UPLOAD_URI = "/guest/entry/file-upload";
	public static final String ADMIN_ENTRY_URI = "/admin/entry";
	public static final String ADMIN_ENTRY_FILE_UPLOAD_URI = "/admin/entry/{id}/file-upload";
	public static final String APPROVE_ENTRY_URI = "/admin/entry/{id}/approve";
	public static final String REJECT_ENTRY_URI = "/admin/entry/{id}/reject";
	public static final String DELETE_ENTRY_URI = "/admin/entry/{id}/delete";
	public static final String USER = "/user";
	public static final String LOGIN = "/login";
}
