package com.example.library;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Centralized REST API exception handling for this project. */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private Map<String, Object> body(HttpStatus status, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("timestamp", LocalDateTime.now().toString());
		response.put("status", status.value());
		response.put("error", status.getReasonPhrase());
		response.put("message", message);
		return response;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body(HttpStatus.NOT_FOUND, ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).distinct()
				.collect(Collectors.joining("; "));
		if (message.isBlank())
			message = "Request validation failed";
		return ResponseEntity.badRequest().body(body(HttpStatus.BAD_REQUEST, message));
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<Map<String, Object>> handleUnreadableBody(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(body(HttpStatus.BAD_REQUEST,
				"Invalid or missing JSON request body. Check JSON syntax and field data types."));
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return ResponseEntity.badRequest()
				.body(body(HttpStatus.BAD_REQUEST, "Invalid value for parameter: " + ex.getName()));
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<Map<String, Object>> handleMissingParameter(MissingServletRequestParameterException ex) {
		return ResponseEntity.badRequest()
				.body(body(HttpStatus.BAD_REQUEST, "Required request parameter is missing: " + ex.getParameterName()));
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		return ResponseEntity.badRequest().body(body(HttpStatus.BAD_REQUEST, ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
		// Avoid exposing internal exception details to API clients.
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(body(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred. Please try again."));
	}
}
