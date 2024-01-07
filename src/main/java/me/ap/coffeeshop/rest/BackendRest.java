package me.ap.coffeeshop.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.util.DBService;
import me.ap.coffeeshop.util.OrdersService;

@Path("/backend")
@RequestScoped
public class BackendRest {

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;
	@Inject
	private OrdersService ordersService;

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
	
	@Path("/placeAnOrder")
	@POST
	public Response placeAnOrder(@QueryParam(value = "coffeeTypeId") Long coffeeTypeId, @QueryParam(value = "tableNumber") Integer tableNumber) {
		
		logger.info("Called place an order endpoint. Params: coffeeTypeId: " + coffeeTypeId + " tableNumber: " +tableNumber);
		Response.ResponseBuilder builder = null;
		if (coffeeTypeId == null) {
			builder = Response.accepted("coffeeTypeId is a required parameter for this action.");
			return builder.build();
		}
		if (tableNumber == null) {
			builder = Response.accepted("tableNumber is a required parameter for this action.");
			return builder.build();
		}
		
		CoffeeType ct = dBService.getCoffeeTypeById(coffeeTypeId);
		if (ct == null) {
			builder = Response.accepted("Coffee type with that ID doesn't exist.");
			return builder.build();
		}
		
		Long orderNumber;
		CoffeeOrder order = new CoffeeOrder();
		order.setCoffeeType(ct);
		order.setTableNumber(tableNumber);
		orderNumber = ordersService.determineMachine(order);
		
		if (orderNumber == null) {
			builder = Response.accepted("There was an error, please try again.");
			return builder.build();
		}
		
		builder = Response.ok(orderNumber);
		return builder.build();
	}

	private Machine getMachineNum(int i) {
		return new Machine(i, 300);
	}

}
