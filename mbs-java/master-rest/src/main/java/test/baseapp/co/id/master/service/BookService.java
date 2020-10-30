package test.baseapp.co.id.master.service;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;

import test.baseapp.co.id.common.rest.server.RestResponse;
import test.baseapp.co.id.model.entity.Book;
import test.baseapp.co.id.model.entity.QBook;
import test.baseapp.co.id.model.repo.BookRepo;
import test.baseapp.co.id.model.util.Constants;

@Service
public class BookService {
	
	private QBook bookBase = QBook.book;
	
	@Autowired
	private BookRepo bookRepo;

	public RestResponse createValidation(Book object) {
		RestResponse<Book> response = new RestResponse<>();
        response.setIsValid(true);
		
		//Mandatory field : can't be empty or null.
		if(object.getTitle().isEmpty()) {
			response.setData(object);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(Constants.EMPTY_DATA_DEFAULT_MESSAGE);
			response.setIsValid(false);
			return response;
		}
		
		//nik must be unique.
        BooleanExpression searchByNamePredicate = bookBase.title.equalsIgnoreCase(object.getTitle());

        if (StreamSupport.stream(bookRepo.findAll(searchByNamePredicate).spliterator(), false).findFirst().isPresent()) {
            response.setData(object);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(Constants.DUPLICATE_DATA_MESSAGE);
            response.setIsValid(false);
            return response;
        }
		
		return response;
	}

	public RestResponse updateValidation(Book object) {
		RestResponse<Book> response = new RestResponse<>();
        response.setIsValid(true);

        //nik must be unique.
        BooleanExpression searchByNamePredicate = bookBase.title.equalsIgnoreCase(object.getTitle())
                .and(bookBase.id.ne(object.getId()));

        if (StreamSupport.stream(bookRepo.findAll(searchByNamePredicate).spliterator(), false).findFirst().isPresent()) {
            response.setData(object);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(Constants.DUPLICATE_DATA_MESSAGE);
            response.setIsValid(false);
            return response;
        }
        
        return response;
	}

}
