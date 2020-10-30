package test.baseapp.co.id.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("book.filter")
@Entity
@Table(name = "book", schema = "perpustakaan")
@Getter
@Setter
public class Book extends BaseEntityMaster {
	private String title;
	private String author;
}
