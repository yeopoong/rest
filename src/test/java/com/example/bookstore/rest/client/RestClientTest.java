package com.example.bookstore.rest.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.bookstore.common.domain.Book;
import com.example.bookstore.rest.domain.BookResource;

public class RestClientTest {

	public static void main(String[] args) throws Exception {
		System.out.println("1111");

		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.setMessageConverters(messageConverters);
		System.out.println("22222");

		Book[] books = restTemplate.getForObject(
				"http://localhost:8080/rest/books", Book[].class);
		List<Book> bookList = Arrays.asList(books);
		for (Book b : books) {
			System.out.println(b);
		}

		Book book = restTemplate
				.getForObject("http://localhost:8080/rest/books/{bookId}",
						Book.class, "1");

		System.out.println(book);

		ParameterizedTypeReference<Resources<BookResource>> typeRef = 
				new ParameterizedTypeReference<Resources<BookResource>>() {};
				
		ResponseEntity<Resources<BookResource>> response = restTemplate
				.exchange("http://localhost:8080/rest/books",
						HttpMethod.GET, null, typeRef);

		for (BookResource b : response.getBody().getContent()) {
			System.out.println(b.getTitle());
		}
		
		//
		// BookResource bookResource = restTemplate
		// .getForObject(
		// "http://localhost:8080/rest/books/{bookId}",
		// BookResource.class, "1");

		// System.out.println(bookResource);
	}

}
