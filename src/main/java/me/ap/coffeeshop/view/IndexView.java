package me.ap.coffeeshop.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.Param;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.model.Refill;
import me.ap.coffeeshop.util.DBService;
import me.ap.coffeeshop.util.OrdersService;
import me.ap.coffeeshop.util.UtilBean;

@Named
@ViewScoped
public class IndexView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;
	@Inject
	private OrdersService ordersService;
	@Inject
	private UtilBean util;

	@Inject
	private FacesContext facesContext;

	@Inject
	@Param(name = "tableNum")
	private Integer tableNum;

	private Machine machine1;
	private Machine machine2;
	private Machine machine3;

	private List<CoffeeOrder> mach1orders;
	private List<CoffeeOrder> mach2orders;
	private List<CoffeeOrder> mach3orders;

	private Integer tableNumber;
	private Integer chTableNumber;
	private String tableNumChangePw;

	private List<CoffeeType> coffeeTypes;

	@PostConstruct
	private void init() {

		machine1 = dBService.loadMachineByNum(1);
		machine2 = dBService.loadMachineByNum(2);
		machine3 = dBService.loadMachineByNum(3);
		coffeeTypes = dBService.getAllActiveCoffeeTypes();
		logger.info("Loaded coffee types: " + coffeeTypes.size());
		updateCurrentOrders();
		logger.info("Index page loaded with table number: " + tableNum);
		tableNumber = tableNum;

	}

	public void placeOrder(CoffeeType ct) {

		if (tableNumber == null) {
			facesContext.addMessage(null, util.errorMsg("You need to select a table number!"));
			return;
		}

		CoffeeOrder order = new CoffeeOrder();
		order.setCoffeeType(ct);
		order.setTableNumber(tableNumber);
		Long orderNumber = ordersService.determineMachine(order);

		if (orderNumber == null) {
			facesContext.addMessage(null, util.errorMsg("Something went wrong with your request, please try again!"));
			return;
		}

		facesContext.addMessage(null, util.successMsg(
				"Your order was successful, the number of your order is " + orderNumber + " Please remember it."));

	}

	public void changeTableNumber() throws IOException {
		if (tableNumChangePw.equals("password123")) {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/coffee-shop/index.xhtml?tableNum=" + chTableNumber);
		}
	}

	public boolean isMachineFree(long machineNum) {

		Machine machine = dBService.loadMachineByNum(machineNum);

		Refill latestRefill = dBService.getLatestRefillForMachine(machine);
		if ((latestRefill != null && ordersService.isRefillInTheFuture(latestRefill)))
			return false;

		CoffeeOrder latestOrder = dBService.getLatestOrderForMachine(machine);
		if (latestOrder == null)
			return true;
		return !ordersService.isOrderInTheFuture(latestOrder);

	}

	public void updateCurrentOrders() {
		mach1orders = dBService.getCurrentCoffeeOrdersForMachine(machine1);
		mach2orders = dBService.getCurrentCoffeeOrdersForMachine(machine2);
		mach3orders = dBService.getCurrentCoffeeOrdersForMachine(machine3);
	}

	public Machine getMachine1() {
		return machine1;
	}

	public void setMachine1(Machine machine1) {
		this.machine1 = machine1;
	}

	public Machine getMachine2() {
		return machine2;
	}

	public void setMachine2(Machine machine2) {
		this.machine2 = machine2;
	}

	public Machine getMachine3() {
		return machine3;
	}

	public void setMachine3(Machine machine3) {
		this.machine3 = machine3;
	}

	public Integer getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(Integer tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getTableNumChangePw() {
		return tableNumChangePw;
	}

	public void setTableNumChangePw(String tableNumChangePw) {
		this.tableNumChangePw = tableNumChangePw;
	}

	public Integer getChTableNumber() {
		return chTableNumber;
	}

	public void setChTableNumber(Integer chTableNumber) {
		this.chTableNumber = chTableNumber;
	}

	public List<CoffeeType> getCoffeeTypes() {
		return coffeeTypes;
	}

	public void setCoffeeTypes(List<CoffeeType> coffeeTypes) {
		this.coffeeTypes = coffeeTypes;
	}

	public List<CoffeeOrder> getMach1orders() {
		return mach1orders;
	}

	public void setMach1orders(List<CoffeeOrder> mach1orders) {
		this.mach1orders = mach1orders;
	}

	public List<CoffeeOrder> getMach2orders() {
		return mach2orders;
	}

	public void setMach2orders(List<CoffeeOrder> mach2orders) {
		this.mach2orders = mach2orders;
	}

	public List<CoffeeOrder> getMach3orders() {
		return mach3orders;
	}

	public void setMach3orders(List<CoffeeOrder> mach3orders) {
		this.mach3orders = mach3orders;
	}

}
