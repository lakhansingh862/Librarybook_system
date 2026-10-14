package com.example.library;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Main application and REST API class. All book endpoint and array-storage
 * logic is implemented in this class.
 */
@SpringBootApplication
@Validated
public class LibraryService {
	public static void main(String[] args) {
		SpringApplication.run(LibraryService.class, args);
	}

	// Array-based storage: holds up to 1000 books while the application is running.
	private final Book[] data = new Book[1000];
	private int size = 0;
	private int nextId = 1;

	private int findIndex(int id) {
		for (int i = 0; i < size; i++)
			if (data[i].getBookId() == id)
				return i;
		return -1;
	}

	private void addToArray(Book book) {
		if (size >= data.length)
			throw new IllegalStateException("Book storage is full");
		data[size++] = book;
	}

	private List<Book> allBooks() {
		return new ArrayList<>(Arrays.asList(Arrays.copyOf(data, size)));
	}

	public ResponseEntity<Book> add(@Valid @RequestBody Book book) {
		book.setBookId(nextId++);
		addToArray(book);
		return ResponseEntity.status(HttpStatus.CREATED).body(book);
	}

	public List<Book> all() {
		return allBooks();
	}

	public ResponseEntity<Book> one(@PathVariable int id) {
		int i = findIndex(id);
		if (i == -1)
			throw new ResourceNotFoundException("Book with ID " + id + " not found");
		return ResponseEntity.ok(data[i]);
	}

	public ResponseEntity<Book> update(@PathVariable int id, @Valid @RequestBody Book book) {
		int i = findIndex(id);
		if (i == -1)
			throw new ResourceNotFoundException("Book with ID " + id + " not found");
		book.setBookId(id);
		data[i] = book;
		return ResponseEntity.ok(book);
	}

	public ResponseEntity<Book> availability(@PathVariable int id, @RequestParam boolean available) {
		int i = findIndex(id);
		if (i == -1)
			throw new ResourceNotFoundException("Book with ID " + id + " not found");
		data[i].setAvailable(available);
		return ResponseEntity.ok(data[i]);
	}

	public ResponseEntity<Void> delete(@PathVariable int id) {
		int i = findIndex(id);
		if (i == -1)
			throw new ResourceNotFoundException("Book with ID " + id + " not found");
		for (int j = i; j < size - 1; j++)
			data[j] = data[j + 1];
		data[--size] = null;
		return ResponseEntity.noContent().build();
	}

	public List<Book> searchByName(@RequestParam String name) {
		List<Book> r = new ArrayList<>();
		for (int i = 0; i < size; i++)
			if (data[i].getBookName().toLowerCase().contains(name.toLowerCase()))
				r.add(data[i]);
		return r;
	}

	public List<Book> searchByAuthor(@RequestParam String author) {
		List<Book> r = new ArrayList<>();
		for (int i = 0; i < size; i++)
			if (data[i].getAuthor().equalsIgnoreCase(author))
				r.add(data[i]);
		return r;
	}

	public List<Book> availableBooks() {
		List<Book> r = new ArrayList<>();
		for (int i = 0; i < size; i++)
			if (data[i].isAvailable())
				r.add(data[i]);
		return r;
	}

	public Map<String, Integer> count() {
		return Map.of("totalBooks", size);
	}

	public Map<String, Double> totalValue() {
		double total = 0;
		for (int i = 0; i < size; i++)
			total += data[i].getPrice();
		return Map.of("totalValue", total);
	}
}
