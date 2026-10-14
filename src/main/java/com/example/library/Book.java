package com.example.library;

import jakarta.validation.constraints.*;

public class Book {
	private Integer bookId;
	@NotBlank(message = "Book name is required")
	private String bookName;
	@NotBlank(message = "Author name is required")
	private String author;
	@Positive(message = "Price must be positive")
	private double price;
	private boolean available = true;

	public Book() {
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer v) {
		bookId = v;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String v) {
		bookName = v;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String v) {
		author = v;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double v) {
		price = v;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean v) {
		available = v;
	}
}