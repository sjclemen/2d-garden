package ca.twodee.persistence.message.tag;

import java.sql.SQLException;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Tag;

public class AddTagRequest extends PersistenceRequest<Tag> {
	private final Tag tag;
	
	public AddTagRequest(Tag tag) {
		this.tag = tag;
	}	

	@Override
	public Tag handleRequest(DaoCacher daoCacher) throws SQLException {
		daoCacher.getTagDao().create(tag);
		return tag;
	}

}
