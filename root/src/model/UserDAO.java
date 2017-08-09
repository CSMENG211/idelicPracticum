package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.User;

public class UserDAO extends GenericDAO<User> {
	public UserDAO(ConnectionPool pool) throws DAOException {
		super(User.class, "user", pool);
	}
	


	public void setPassword(String userName, String password) throws RollbackException {
        try {
        	Transaction.begin();
			User dbUser = read(userName);
			
			if (dbUser == null) {
				throw new RollbackException("User "+userName+" no longer exists");
			}
			
			dbUser.encodePassword(password);
			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
}
