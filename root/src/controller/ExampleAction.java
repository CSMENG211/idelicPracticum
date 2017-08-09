package controller;

import javax.servlet.http.HttpServletRequest;

import model.Model;

public class ExampleAction extends Action {

	
	public ExampleAction(Model model){
	// Initialize the DAO object in the constructor	
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "home.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "index.html";
	}

}
