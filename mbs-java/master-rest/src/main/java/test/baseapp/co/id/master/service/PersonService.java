package test.baseapp.co.id.master.service;

import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;

import test.baseapp.co.id.common.rest.server.RestResponse;
import test.baseapp.co.id.model.entity.Person;
import test.baseapp.co.id.model.entity.QPerson;
import test.baseapp.co.id.model.repo.PersonRepo;
import test.baseapp.co.id.model.util.Constants;

@Service
public class PersonService {
	
	private QPerson personBase = QPerson.person;
	
	@Autowired
	private PersonRepo personRepo;
	
	public RestResponse createValidation(Person object) {
		
		RestResponse<Person> response = new RestResponse<>();
        response.setIsValid(true);
		
		//Mandatory field : can't be empty or null.
		if(object.getFirstName().isEmpty()) {
			response.setData(object);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(Constants.EMPTY_DATA_DEFAULT_MESSAGE);
			response.setIsValid(false);
			return response;
		}
		
		//nik must be unique.
        BooleanExpression searchByNamePredicate = personBase.nik.equalsIgnoreCase(object.getNik());

        if (StreamSupport.stream(personRepo.findAll(searchByNamePredicate).spliterator(), false).findFirst().isPresent()) {
            response.setData(object);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(Constants.DUPLICATE_DATA_MESSAGE);
            response.setIsValid(false);
            return response;
        }
		
		return response;
		
	}

	public RestResponse updateValidation(Person object) {
		RestResponse<Person> response = new RestResponse<>();
        response.setIsValid(true);

        //nik must be unique.
        BooleanExpression searchByNamePredicate = personBase.nik.equalsIgnoreCase(object.getNik())
                .and(personBase.id.ne(object.getId()));

        if (StreamSupport.stream(personRepo.findAll(searchByNamePredicate).spliterator(), false).findFirst().isPresent()) {
            response.setData(object);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(Constants.DUPLICATE_DATA_MESSAGE);
            response.setIsValid(false);
            return response;
        }
        
        return response;
	}

}
