package test.baseapp.co.id.common.stereotype.exception.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.exception.RootCauseDetail;

@SuppressWarnings("rawtypes")
public interface RootCauseDetailRepo<RD extends RootCauseDetail, ID extends Serializable> extends CrudRepository<RD, ID> {

}
