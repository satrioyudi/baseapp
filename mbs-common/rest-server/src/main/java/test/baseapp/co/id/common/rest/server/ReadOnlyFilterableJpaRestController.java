package test.baseapp.co.id.common.rest.server;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.querydsl.core.types.dsl.EntityPathBase;

public abstract class ReadOnlyFilterableJpaRestController<T, ID extends Serializable, Q extends EntityPathBase<T>> extends FilterableJpaRestController<T, ID, Q>
{
	@Override
	public ResponseEntity<T> create(T object)
	{
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}

	@Override
	public ResponseEntity<T> update(ID id, T object)
	{
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}

	@Override
	public ResponseEntity<Void> delete(ID id)
	{
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}	
}
