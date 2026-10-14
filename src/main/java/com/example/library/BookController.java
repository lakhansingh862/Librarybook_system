package com.example.library;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
	private final LibraryService server;

	public BookController(LibraryService server) {
		this.server = server;
	}

	@PostMapping("/add")
	public ResponseEntity<Book> add(@Valid @RequestBody Book book) {
		return server.add(book);
	}

	@GetMapping("/all")
	public List<Book> all() {
		return server.all();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> one(@PathVariable int id) {
		return server.one(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> update(@PathVariable int id, @Valid @RequestBody Book book) {
		return server.update(id, book);
	}

	@PutMapping("/{id}/availability")
	public ResponseEntity<Book> availability(@PathVariable int id, @RequestParam boolean available) {
		return server.availability(id, available);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		return server.delete(id);
	}

	@GetMapping("/search/name")
	public List<Book> searchByName(@RequestParam String name) {
		return server.searchByName(name);
	}

	@GetMapping("/search/author")
	public List<Book> searchByAuthor(@RequestParam String author) {
		return server.searchByAuthor(author);
	}

	@GetMapping("/available")
	public List<Book> availableBooks() {
		return server.availableBooks();
	}

	@GetMapping("/count")
	public Map<String, Integer> count() {
		return server.count();
	}

	@GetMapping("/total-value")
	public Map<String, Double> totalValue() {
		return server.totalValue();
	}
}
