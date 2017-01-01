package backend;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -5687014202937213406L;
	
	public String request, response;
	
	public String getRequest() { return request; }
	public String getResponse() { return response; }
	public void setRequest(String request) { this.request = request; }
	public void setResponse(String response) { this.response = response; }
	
}
