package test.baseapp.co.id.common.rest.server;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ReadOnlyJpaRestController<T, ID extends Serializable> extends JpaRestController<T, ID>
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
