package test.baseapp.co.id.common.stereotype.content.repo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.content.Content;

public interface ContentRepo<C extends Content, ID extends Serializable> extends CrudRepository<C, ID>
{

}
