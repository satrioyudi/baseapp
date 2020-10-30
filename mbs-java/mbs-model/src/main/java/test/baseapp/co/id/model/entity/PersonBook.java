package test.baseapp.co.id.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("personBook.filter")
@Getter @Setter
@Entity
@Table(name = "person_book", schema = "perpustakaan")
public class PersonBook {

	@JsonUnwrapped
	@EmbeddedId
	private PersonBookId personBookId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	@MapsId("personId")
	private Person person;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	@MapsId("bookId")
	private Book book;
}
