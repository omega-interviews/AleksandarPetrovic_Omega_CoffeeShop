package me.ap.coffeeshop.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.util.DBService;
import me.ap.coffeeshop.util.OrdersService;

@Path("/order")
@RequestScoped
public class OrderRestService {

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;
	@Inject
	private OrdersService ordersService;

	@POST
	public Response coffeeToGo(@QueryParam(value = "coffeTypeId") int coffeTypeId) {

		logger.info("New order REST called, params: coffeeTypeId=" + coffeTypeId);
		CoffeeType ct = dBService.getCoffeeTypeById(coffeTypeId);

		Response.ResponseBuilder builder = null;

		if (ct == null) {
			builder = Response.accepted("Coffee type with that ID doesn't exist.");
			return builder.build();
		}

		if (ct.isDeleted()) {
			builder = Response.accepted("Coffee type is no longer active.");
			return builder.build();
		}

		if (dBService.getCurrentCoffeeToGoOrders().size() >= 5) {
			builder = Response.accepted("There are already 5 active orders for coffees to go. Please try again.");
			return builder.build();
		}

		CoffeeOrder order = new CoffeeOrder();
		order.setCoffeeType(ct);
		order.setTableNumber(0);
		Long result = ordersService.determineMachine(order);

		if (result != null)
			builder = Response.ok(result);
		else
			builder = Response.accepted("The request went wrong. Please try again!");

		return builder.build();
	}

}
