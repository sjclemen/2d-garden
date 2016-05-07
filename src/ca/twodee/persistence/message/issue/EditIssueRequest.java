package ca.twodee.persistence.message.issue;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Issue;

// TODO: WE SHOULD NOT OVERWRITE NEWER WRITES TO THE SAME ROW
// i.e. they could be reordered or one write thread could take forever
// TODO: use Concurrent Hashmap to solve this? will it work?
public class EditIssueRequest extends PersistenceRequest <Void> {
	private final Issue issue;
	
	public EditIssueRequest(Issue issue) {
		this.issue = issue;
	}
	
	@Override
	public Void handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<Issue, Integer> issueDao = daoCacher.getIssueDao();
		int result = issueDao.update(issue);
		if (result != 1) {
			throw new SQLException("EditIssueRequest: Expected 1 row returned, got " + result);
		}
		
		return null;
	}

}
