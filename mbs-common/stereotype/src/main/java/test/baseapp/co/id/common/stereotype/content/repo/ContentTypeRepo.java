package test.baseapp.co.id.common.stereotype.content.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.content.ContentType;

public interface ContentTypeRepo<CT extends ContentType, ID extends Serializable> extends CrudRepository<CT, ID>
{

}
