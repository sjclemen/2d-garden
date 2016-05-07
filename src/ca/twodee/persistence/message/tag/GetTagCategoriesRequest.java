package ca.twodee.persistence.message.tag;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.TagCategory;

public class GetTagCategoriesRequest extends PersistenceRequest <List <TagCategory>> {

	@Override
	public List<TagCategory> handleRequest(DaoCacher daoCacher)
			throws SQLException {
		Dao<TagCategory, Integer> tagCategoryDao = daoCacher.getTagCategoryDao();
		
		List<TagCategory> results;
		results = tagCategoryDao.queryForAll();
		return results;
	}

}
