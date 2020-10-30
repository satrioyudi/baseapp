package test.baseapp.co.id.model.entity;

import lombok.Setter;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter @Setter
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
}
