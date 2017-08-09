package model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;

public class Model {
	
	private UserDAO userDAO;
	private LogDAO logDAO;

	public Model(ServletConfig config) throws ServletException {
		String jdbcDriver = config.getInitParameter("jdbcDriverName");
	    String jdbcURL    = config.getInitParameter("jdbcURL");
	    
	    try {
		    ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
	
	    	userDAO  = new UserDAO(pool);
	    	logDAO = new LogDAO(pool);
	    
	    } catch (DAOException e) {
	    	throw new ServletException(e);
	    }
	}
	
	
	public UserDAO getUserDAO() { return userDAO;}
	public LogDAO getLogDAO()	{return logDAO;}
}
