package com.fox.shared.ulimit;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Servlet implementation class TestBTMLogger
 */
public class TestBTMLogger extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public TestBTMLogger() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		PrintWriter out = response.getWriter();
		try{
			System.out.println("Servlet: BTM Logger call started.. ");
			response.setContentType("text/html");			
			out.println("<h1> Outoput of BTM Logger Call </h1>");
			out.println("<h4>"+ processRequest() +"</h4>");
			System.out.println("Servlet: BTM Logger call done.. ");	
		}catch(Exception e){
			out.println("<h4>");
			e.printStackTrace(out);
			out.println("</h4>");
		}
	}

	static String request = "{\"test\": \"test123\"}";
	static String url = "https://ms-qaapi-internal.foxinc.com/btm/logger/publish";
	static String response = "";

	public static String processRequest() throws Exception {
		System.out.println("In common util - callout processor : received request ");
		Client client = Client.create();
		ClientResponse clientResponse = null;
		try {
			client.addFilter(new LoggingFilter(System.out));
			client.setConnectTimeout(10000);
			client.setReadTimeout(20000);

			WebResource webResource = client.resource(url);

			MultivaluedMap<String, String> params = new MultivaluedMapImpl();
			webResource = webResource.queryParams(params);

			Builder reqBuilder = webResource.getRequestBuilder();
			System.out.println("Invoking REST service");
			response = reqBuilder.type("application/json").method("POST", String.class, request);
		} catch (Exception e) {
			e.printStackTrace();
			response = e.toString();
			System.out.println("Exception occurred while calling btm logger. " + e);
			throw e;
		} finally {

			if (client != null) {
				client.destroy();

			}
			if (clientResponse != null) {
				clientResponse.close();
			}
		}
		return response;
	}
}
