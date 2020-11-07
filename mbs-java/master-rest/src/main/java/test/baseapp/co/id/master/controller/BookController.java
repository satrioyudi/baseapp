package test.baseapp.co.id.master.controller;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.dsl.BooleanExpression;

import test.baseapp.co.id.common.rest.server.FilterableJpaRestController;
import test.baseapp.co.id.common.rest.server.RestFilter;
import test.baseapp.co.id.common.rest.server.RestResponse;
import test.baseapp.co.id.master.service.BookService;
import test.baseapp.co.id.model.entity.Book;
import test.baseapp.co.id.model.entity.QBook;
import test.baseapp.co.id.model.util.Constants;

@RestController
@RequestMapping("${rest.pathPrefix:api}/book")
public class BookController extends FilterableJpaRestController<Book, BigInteger, QBook>{
	
	@Autowired
	private BookService bookService;
	
	@Override
	protected QBook getPathBase() {
		return QBook.book;
	}
	
	@Override
	protected Book doCreate(Book book) {
		return super.doCreate(book);
	}

	@Override
	protected BooleanExpression createPredicate(QBook pathBase, RestFilter filter) {
		String value =  Optional.ofNullable((String) filter.getValue()).orElse("");
        Boolean isActiveSearch = Optional.ofNullable(Constants.ACTIVE_MAP.get(value.toLowerCase())).orElse(null);

        switch (filter.getPath()) {
            case "title":
                return pathBase.title.containsIgnoreCase(value);
            case "author":
                return pathBase.author.containsIgnoreCase(value);
            case "updatedBy":
                return pathBase.updatedBy.containsIgnoreCase(value);
            case "filterByAll":

                BooleanExpression booleanPredicate = isActiveSearch == null ? null : pathBase.isActive.eq(isActiveSearch);

                BooleanExpression predicate = pathBase.author.containsIgnoreCase(value)
                        .or(pathBase.title.containsIgnoreCase(value));

                return booleanPredicate == null ? predicate : booleanPredicate.or(predicate);

            default:
                return null;
        }
	}

	@Override
	protected RestResponse<Book> createValidation(Book object) {
		return bookService.createValidation(object);
	}

	@Override
	protected RestResponse<Book> updateValidation(Book object) {
		return bookService.updateValidation(object);
	}
	
	@Override
	protected Book save(BigInteger id, Book object) {
		return super.save(id, object);
		
	}

}
