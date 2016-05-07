package ca.twodee.persistence.message.issue;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Issue;
import ca.twodee.schema.IssueTag;
import ca.twodee.schema.Tag;

public class EditIssueTagsRequest extends PersistenceRequest <Void> {
	private final Integer issueId;
	private final List<Integer> tagsToRemove;
	private final List<Integer> tagsToAdd;
	
	public EditIssueTagsRequest(Integer issueId, List<Integer> tagsToRemove, List<Integer> tagsToAdd) {
		this.issueId = issueId;
		this.tagsToRemove = tagsToRemove;
		this.tagsToAdd = tagsToAdd;
	}
	
	@Override
	public Void handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<IssueTag, Integer> issueTagDao = daoCacher.getIssueTagDao();
		if (!tagsToRemove.isEmpty()) {
			DeleteBuilder<IssueTag, Integer> deleteBuilder = issueTagDao.deleteBuilder();
			deleteBuilder.where().eq(IssueTag.ISSUE_ID_FN, issueId)
				.and().in(IssueTag.TAG_ID_FN, tagsToRemove);
			issueTagDao.delete(deleteBuilder.prepare());
		}
		
		if (!tagsToAdd.isEmpty()) {
			Issue issue = daoCacher.getIssueDao().queryForId(issueId);
			
			for (Integer tagId : tagsToAdd) {
				Tag tag = daoCacher.getTagDao().queryForId(tagId);
				IssueTag newIssueTag = new IssueTag(issue, tag);
				issueTagDao.create(newIssueTag);
			}
		}
		
		return null;
	}

}
