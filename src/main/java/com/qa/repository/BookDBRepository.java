package com.qa.repository;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.Collection;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.qa.domain.Book;
import com.qa.util.JSONUtil;

@Transactional(SUPPORTS)
@Default
public class BookDBRepository implements BookRepository 
{

	@PersistenceContext(unitName = "primary")
	private EntityManager manager;

	@Inject
	private JSONUtil util;

	@Override
	public String getAllBooks() {
		Query query = manager.createQuery("Select a FROM Account a");
		Collection<Book> accounts = (Collection<Book>) query.getResultList();
		return util.getJSONForObject(accounts);
	}

	@Override
	@Transactional(REQUIRED)
	public String createBook(String book) {
		Book aBook = util.getObjectForJSON(book, Book.class);
		manager.persist(aBook);
		return "{\"message\": \"account has been sucessfully added\"}";
	}

	@Override
	@Transactional(REQUIRED)
	public String updateBook(Long id, String bookToUpdate) {
		Book updatedBook = util.getObjectForJSON(bookToUpdate, Book.class);
		Book bookFromDB = findBook(id);
		if (bookToUpdate != null) {
			bookFromDB = updatedBook;
			manager.merge(bookFromDB);
		}
		return "{\"message\": \"account sucessfully updated\"}";
	}

	@Override
	@Transactional(REQUIRED)
	public String deleteBook(Long id) {
		Book bookInDB = findBook(id);
		if (bookInDB != null) {
			manager.remove(bookInDB);
			return "{\"message\": \"account sucessfully deleted\"}";
		}
		else
		{
			return "{\"message\": \"account not \"}";
		}
	}

	private Book findBook(Long id) {
		return manager.find(Book.class, id);
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public void setUtil(JSONUtil util) {
		this.util = util;
	}

}
