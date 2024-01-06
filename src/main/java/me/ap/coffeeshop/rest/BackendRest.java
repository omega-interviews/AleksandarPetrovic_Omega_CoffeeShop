package me.ap.coffeeshop.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.util.DBService;

@Path("/backend")
@RequestScoped
public class BackendRest {

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;

	@Path("currentOrders")
	@GET
	@Produces("application/json")
	public Response getCurrentOrders() {
		logger.info("Called current orders endpoint");
		JSONArray jsonarray = new JSONArray();
		List<CoffeeOrder> coffeeOrders = dBService.getCurrentCoffeeOrdersForMachine(getMachineNum(1));
		makeJsonArray(jsonarray, coffeeOrders);
		coffeeOrders = dBService.getCurrentCoffeeOrdersForMachine(getMachineNum(2));
		makeJsonArray(jsonarray, coffeeOrders);
		coffeeOrders = dBService.getCurrentCoffeeOrdersForMachine(getMachineNum(3));
		makeJsonArray(jsonarray, coffeeOrders);
		Response.ResponseBuilder builder = null;
		builder = Response.ok(jsonarray.toString());
		return builder.build();
	}

	private void makeJsonArray(JSONArray jsonarray, List<CoffeeOrder> coffeeOrders) {
		for (CoffeeOrder coffeeOrder : coffeeOrders) {
			JSONObject jsonobject = new JSONObject();
			jsonobject.put("id", coffeeOrder.getId());
			jsonobject.put("machine", coffeeOrder.getMachine().getId());
			jsonobject.put("tableNumber", coffeeOrder.getTableNumber());
			jsonobject.put("time", coffeeOrder.getTime().plusSeconds(coffeeOrder.getCoffeeType().getPreparationTime()));
			jsonarray.put(jsonobject);
		}
	}

	private Machine getMachineNum(int i) {
		return new Machine(i, 300);
	}

}
