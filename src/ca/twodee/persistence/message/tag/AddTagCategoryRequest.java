package ca.twodee.persistence.message.tag;

import java.sql.SQLException;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.TagCategory;

public class AddTagCategoryRequest extends PersistenceRequest<TagCategory> {
	private final TagCategory tagCategory;
	
	public AddTagCategoryRequest(TagCategory tagCategory) {
		this.tagCategory = tagCategory;
	}

	@Override
	public TagCategory handleRequest(DaoCacher daoCacher) throws SQLException {
		daoCacher.getTagCategoryDao().create(tagCategory);
		return tagCategory;
	}

}
