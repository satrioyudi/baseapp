package test.baseapp.co.id.master.controller;

import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

import test.baseapp.co.id.common.rest.server.FilterableJpaRestController;
import test.baseapp.co.id.common.rest.server.RestFilter;
import test.baseapp.co.id.common.rest.server.RestResponse;
import test.baseapp.co.id.master.service.PersonService;
import test.baseapp.co.id.model.entity.Person;
import test.baseapp.co.id.model.entity.PersonBook;
import test.baseapp.co.id.model.entity.QPerson;
import test.baseapp.co.id.model.repo.BookRepo;
import test.baseapp.co.id.model.repo.PersonRepo;
import test.baseapp.co.id.model.util.Constants;


@RestController
@RequestMapping("/api/person")
public class PersonController extends FilterableJpaRestController<Person, BigInteger, QPerson>{

	@Autowired
	private PersonService personService;
	
	@Autowired
	private BookRepo bookRepo;
	
	@Override
	protected QPerson getPathBase() {
		return QPerson.person;
	}
	
	@Override
	protected Person doCreate(Person person) {
		return super.doCreate(person);
	}

	@Override
	protected BooleanExpression createPredicate(QPerson pathBase, RestFilter filter) {
		String value =  Optional.ofNullable((String) filter.getValue()).orElse("");
        Boolean isActiveSearch = Optional.ofNullable(Constants.ACTIVE_MAP.get(value.toLowerCase())).orElse(null);

        switch (filter.getPath()) {
            case "firstName":
                return pathBase.firstName.containsIgnoreCase(value);
            case "lastName":
                return pathBase.lastName.containsIgnoreCase(value);
            case "updatedBy":
                return pathBase.updatedBy.containsIgnoreCase(value);
            case "filterByAll":

                BooleanExpression booleanPredicate = isActiveSearch == null ? null : pathBase.isActive.eq(isActiveSearch);

                BooleanExpression predicate = pathBase.firstName.containsIgnoreCase(value)
                        .or(pathBase.lastName.containsIgnoreCase(value))
                        .or(pathBase.updatedBy.containsIgnoreCase(value));

                return booleanPredicate == null ? predicate : booleanPredicate.or(predicate);

            default:
                return null;
        }
	}
	
	@Override
    protected RestResponse<Person> createValidation(Person object) {
        return personService.createValidation(object);
    }

    @Override
    protected RestResponse<Person> updateValidation(Person object) {
        return personService.updateValidation(object);
    }
    
    @Override
    protected Person save(BigInteger id, Person object) {
    	if (!object.getPersonBooks().isEmpty()) {
    		try {
	    		List<PersonBook> pbs = new ArrayList<PersonBook>(object.getPersonBooks().stream().distinct().collect(Collectors.toList()));
				for (PersonBook pb : pbs) {
					pb.setPerson(object);
					pb.setBook(bookRepo.findById(pb.getPersonBookId().getBookId()).get());
					pb.setCheckinDate(pb.getCheckinDate());
					pb.setCheckoutDate(pb.getCheckoutDate());
					pb.setEstimatedDay(pb.getEstimatedDay());
				}
				object.getPersonBooks().clear();
				object.setPersonBooks(new HashSet<PersonBook>(pbs));
				return super.save(id, object);
    		} catch (Exception e) {
    			throw new RuntimeException(e);
			}
		}
    	return super.save(id, object);
    }

}
