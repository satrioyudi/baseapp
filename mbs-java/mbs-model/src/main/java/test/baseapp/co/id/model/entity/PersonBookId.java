package test.baseapp.co.id.model.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class PersonBookId implements Serializable {
	private BigInteger personId;
	private BigInteger bookId;
}
