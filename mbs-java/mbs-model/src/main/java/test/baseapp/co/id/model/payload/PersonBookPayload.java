package test.baseapp.co.id.model.payload;

import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import test.baseapp.co.id.model.entity.PersonBook;

@Getter
@Setter
public class PersonBookPayload {
	private String firstName;
	private String lastName;
	private String nik;
	private Set<PersonBook> personBooks;
	private Date checkinDate;
	private Date checkoutDate;
	private int estimatedDay;
}
