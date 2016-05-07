package ca.twodee.persistence.message.issue;

import java.sql.SQLException;
import java.util.List;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Issue;

import com.j256.ormlite.dao.Dao;

public class GetIssuesRequest extends PersistenceRequest <List <Issue>> {

	@Override
	public List <Issue> handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<Issue, Integer> issueDao = daoCacher.getIssueDao();
		List<Issue> results = issueDao.queryForAll();
		return results;
	}

}
