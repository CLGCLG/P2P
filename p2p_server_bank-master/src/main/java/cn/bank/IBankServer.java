package cn.bank;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/bankServer")
public interface IBankServer {

	@Path("/charge")
	@POST
	public boolean charge(String param);
	
	@Path("/show")
	@GET
	public void show();
}
