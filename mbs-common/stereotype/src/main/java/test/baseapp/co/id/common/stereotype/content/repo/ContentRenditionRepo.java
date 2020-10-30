package test.baseapp.co.id.common.stereotype.content.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import test.baseapp.co.id.common.stereotype.content.Content;
import test.baseapp.co.id.common.stereotype.content.ContentRendition;

@SuppressWarnings("rawtypes")
public interface ContentRenditionRepo<CR extends ContentRendition, ID extends Serializable> extends CrudRepository<CR, ID>
{
	public List<CR> findByOwningContent(Content content);
}
