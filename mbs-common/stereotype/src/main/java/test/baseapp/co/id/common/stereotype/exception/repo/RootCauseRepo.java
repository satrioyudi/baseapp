package test.baseapp.co.id.common.stereotype.exception.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.exception.RootCause;

@SuppressWarnings("rawtypes")
public interface RootCauseRepo<R extends RootCause, ID extends Serializable> extends CrudRepository<R, ID> {

}
