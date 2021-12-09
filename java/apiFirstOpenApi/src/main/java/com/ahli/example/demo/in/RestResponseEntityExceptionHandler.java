package com.ahli.example.demo.in;

import com.ahli.example.gen.model.ErrorDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleOtherExceptions(final Exception ex, final WebRequest request) {
		final String msg = "Service encountered unexpected error";
		log.error("Encountered unexpected error while processing request", ex);
		return handleExceptionInternal(
				ex,
				error(HttpStatus.INTERNAL_SERVER_ERROR, msg),
				utf8json(new HttpHeaders()),
				HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}
	
	private ErrorDto error(final HttpStatus status, final String msg) {
		return new ErrorDto(status.value(), msg);
	}
	
	private HttpHeaders utf8json(final HttpHeaders headers) {
		headers.add(
				HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON_VALUE + ";charset=" + StandardCharsets.UTF_8.name());
		
		return headers;
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			@NonNull final HttpMessageNotReadableException ex,
			@NonNull final HttpHeaders headers,
			@NonNull final HttpStatus status,
			@NonNull final WebRequest request) {
		
		String msg = "Request could not be validated: ";
		final Throwable cause = ex.getCause();
		
		//invalid json
		if (cause instanceof JsonParseException parseException) {
			msg += parseException.getOriginalMessage();
		}
		// e.g. wrong data type
		else if (cause instanceof JsonMappingException jsonMappingException) {
			final List<JsonMappingException.Reference> path = jsonMappingException.getPath();
			if (!path.isEmpty()) {
				msg += String.format("'%s' - could not be processed", path.get(0).getFieldName());
			} else {
				msg += "Payload could not be processed";
			}
		} else {
			msg += "Payload could not be processed";
		}
		
		log.warn(msg, ex);
		return handleExceptionInternal(ex, error(status, msg), utf8json(headers), status, request);
	}
	
	@Override
	@NonNull
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull final MethodArgumentNotValidException ex,
			@NonNull final HttpHeaders headers,
			@NonNull final HttpStatus status,
			@NonNull final WebRequest webRequest) {
		
		String msg = "Request could not be validated";
		final FieldError fieldError = ex.getFieldError();
		if (fieldError != null) {
			msg += String.format(": '%s' - %s", fieldError.getField(), fieldError.getDefaultMessage());
			log.warn("Request has an invalid parameter '{}': ", fieldError, ex);
		} else {
			log.warn("Request could not be validated: ", ex);
		}
		return handleExceptionInternal(ex, error(status, msg), utf8json(headers), status, webRequest);
	}
	
}
