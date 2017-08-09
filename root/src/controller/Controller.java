package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import databean.User;
import model.Model;

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Model model;
	
    public void init() throws ServletException {
        model = new Model(getServletConfig());

        Action.add(new ExampleAction(model));
       
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	String nextPage = performTheAction(request);
	    sendToNextPage(nextPage,request,response);
    }
    
    /*
     * Extracts the requested action and (depending on whether the user is logged in)
     * perform it (or make the user login).
     * @param request
     * @return the next page (the view)
     */
    private String performTheAction(HttpServletRequest request) {
//        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
//        User        user        = (User) session.getAttribute("user");
        String      actionName  = getActionName(servletPath);
        
//        // Write all actions to a log
//        try {
//        	model.getLogDAO().write(request, "performTheAction: " + user + ", action=" + actionName);
//        
//	       
//        } catch (RollbackException e) {
//        	// Perhaps the DB is down
//        	e.printStackTrace();
//        	request.setAttribute("dbError",e.getMessage());
//	        return "db-error.jsp";
//        }
        
      	// Let the local user run his chosen action
		return Action.perform(actionName,request);
    }
    
    /*
     * If nextPage is null, send back 404
     * If nextPage starts with a '/', redirect to this page.
     *    In this case, the page must be the whole servlet path including the webapp name
     * Otherwise dispatch to the page (the view)
     *    This is the common case
     * Note: If nextPage equals "image", we will dispatch to /image.  In the web.xml file, "/image"
     *    is mapped to the ImageServlet will which return the image bytes for display.
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	// System.out.println("nextPage="+nextPage);
    	
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.charAt(0) == '/') {
    		String proto = request.isSecure() ? "https://" : "http://";
			String host  = request.getServerName();
			String port  = ":"+String.valueOf(request.getServerPort());
			if (port.equals(":80")) port = "";
			if (port.equals(":443")) port = "";
			String context = request.getContextPath();
			int lastSlash = context.lastIndexOf('/');
			String prefix = ( lastSlash==0 ? context : context.substring(0,lastSlash) );
			response.sendRedirect(proto+host+port+prefix+nextPage);
			return;
    	}
    	
   		RequestDispatcher d = request.getRequestDispatcher("/WEB-INF/view/"+nextPage);
   		d.forward(request,response);
    }

	/*
	 * Returns the path component after the last slash
	 */
    private String getActionName(String path) {
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
}