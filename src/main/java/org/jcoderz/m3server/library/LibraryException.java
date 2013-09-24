/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcoderz.m3server.library;

/**
 * The library runtime exception.
 */
@SuppressWarnings("serial")
public class LibraryException extends RuntimeException {
	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public LibraryException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 * @param th the cause of the exception
	 */
	public LibraryException(String message, Throwable th) {
		super(message, th);
	}
}
