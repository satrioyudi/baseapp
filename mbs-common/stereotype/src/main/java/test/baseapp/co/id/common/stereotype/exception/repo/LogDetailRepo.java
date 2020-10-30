package test.baseapp.co.id.common.stereotype.exception.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.exception.LogDetail;

@SuppressWarnings("rawtypes")
public interface LogDetailRepo<LD extends LogDetail, ID extends Serializable> extends CrudRepository<LD, ID> {

}
