package ca.twodee.persistence.message.tag;

import java.sql.SQLException;
import java.util.List;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Tag;

import com.j256.ormlite.dao.Dao;

public class GetTagsRequest extends PersistenceRequest <List <Tag>> {

	@Override
	public List <Tag> handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<Tag, Integer> tagDao = daoCacher.getTagDao();
		
		List<Tag> results;
		results = tagDao.queryForAll();
		return results;
	}

}
