package test.baseapp.co.id.common.stereotype.exception.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.exception.Log;

public interface LogRepo <L extends Log, ID extends Serializable> extends CrudRepository<L, ID>{

}
