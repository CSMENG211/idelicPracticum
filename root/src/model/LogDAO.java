package model;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.LogRec;
import databean.User;

public class LogDAO extends GenericDAO<LogRec> {
	public LogDAO(ConnectionPool pool) throws DAOException {
		super(LogRec.class, "log", pool);
	}

	public LogRec write(HttpServletRequest request, String text)
			throws RollbackException {
		try {
			Transaction.begin();
			LogRec rec = new LogRec();
			rec.setIpAddr(request.getRemoteAddr());
			rec.setTime(new Date());
			rec.setText(text);

			User user = (User) request.getSession(true).getAttribute("user");
			String userName = user == null ? null : user.getUserName();
			rec.setUserName(userName);

			create(rec);

			Transaction.commit();

			return rec;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}
}