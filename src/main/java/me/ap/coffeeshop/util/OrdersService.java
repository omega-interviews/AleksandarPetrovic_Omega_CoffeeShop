package me.ap.coffeeshop.util;

import java.time.LocalDateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.model.Refill;

@Stateless
public class OrdersService {

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;

	public boolean determineMachine(CoffeeOrder order) {
		
		checkIfRefillsNeeded(order.getCoffeeType());

		LocalDateTime mach1busyUntil = null;
		Refill latestRefillMach1 = dBService.getLatestRefillForMachine(getMachObject(1));
		if (latestRefillMach1 != null && isRefillInTheFuture(latestRefillMach1))
			mach1busyUntil = latestRefillMach1.getTime().plusSeconds(120);
		CoffeeOrder latestOrderMach1 = dBService.getLatestOrderForMachine(getMachObject(1));
		if (latestOrderMach1 != null && isOrderInTheFuture(latestOrderMach1))
			mach1busyUntil = latestOrderMach1.getTime()
					.plusSeconds(latestOrderMach1.getCoffeeType().getPreparationTime());

		// if machine 1 is free, place order
		if (mach1busyUntil == null)
			return placeOrderToMachine(order, getMachObject(1), null);

		LocalDateTime mach2busyUntil = null;
		Refill latestRefillMach2 = dBService.getLatestRefillForMachine(getMachObject(2));
		if (latestRefillMach2 != null && isRefillInTheFuture(latestRefillMach2)) {
			mach2busyUntil = latestRefillMach2.getTime().plusSeconds(120);
		}
		CoffeeOrder latestOrderMach2 = dBService.getLatestOrderForMachine(getMachObject(2));
		if (latestOrderMach2 != null && isOrderInTheFuture(latestOrderMach2)) {
			mach2busyUntil = latestOrderMach2.getTime()
					.plusSeconds(latestOrderMach2.getCoffeeType().getPreparationTime());
		}

		// if machine 2 is free, place order
		if (mach2busyUntil == null)
			return placeOrderToMachine(order, getMachObject(2), null);

		LocalDateTime mach3busyUntil = null;
		Refill latestRefillMach3 = dBService.getLatestRefillForMachine(getMachObject(3));
		if (latestRefillMach3 != null && isRefillInTheFuture(latestRefillMach3)) {
			mach3busyUntil = latestRefillMach3.getTime().plusSeconds(120);
		}
		CoffeeOrder latestOrderMach3 = dBService.getLatestOrderForMachine(getMachObject(3));
		if (latestOrderMach3 != null && isOrderInTheFuture(latestOrderMach3)) {
			mach3busyUntil = latestOrderMach3.getTime()
					.plusSeconds(latestOrderMach3.getCoffeeType().getPreparationTime());
		}

		// if machine 3 is free, place order
		if (mach3busyUntil == null)
			return placeOrderToMachine(order, getMachObject(3), null);

		// if all 3 are busy, find earliest free
		if (mach1busyUntil.compareTo(mach2busyUntil) < 0) {
			if (mach1busyUntil.compareTo(mach3busyUntil) > 0) {
				return placeOrderToMachine(order, getMachObject(3), mach3busyUntil);
			} else {
				return placeOrderToMachine(order, getMachObject(1), mach1busyUntil);
			}
		} else {
			if (mach2busyUntil.compareTo(mach3busyUntil) > 0) {
				return placeOrderToMachine(order, getMachObject(3), mach3busyUntil);
			} else {
				return placeOrderToMachine(order, getMachObject(2), mach2busyUntil);
			}
		}
	}

	private boolean placeOrderToMachine(CoffeeOrder o, Machine m, LocalDateTime t) {
		Refill latestRefillForMach = dBService.getLatestRefillForMachine(m);
		if (latestRefillForMach != null && isRefillInTheFuture(latestRefillForMach))
			return false;
		o.setMachine(m);
		if (t != null)
			o.setTime(t);
		else
			o.setTime(LocalDateTime.now());
		dBService.saveOrder(o);
		return true;
	}

	public boolean isRefillInTheFuture(Refill refill) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime refillEnd = refill.getTime().plusSeconds(120);
		if (refillEnd.compareTo(now) > 0)
			return true;
		else
			return false;
	}

	public boolean isOrderInTheFuture(CoffeeOrder order) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime orderEnd = order.getTime().plusSeconds(order.getCoffeeType().getPreparationTime());
		if (orderEnd.compareTo(now) > 0)
			return true;
		else
			return false;
	}

	private void checkIfRefillsNeeded(CoffeeType ct) {

		Refill latestRefillMach1 = dBService.getLatestRefillForMachine(getMachObject(1));
		if ((latestRefillMach1 != null && !isRefillInTheFuture(latestRefillMach1)) || latestRefillMach1 == null) {
			long i1 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach1, getMachObject(1));
			logger.info("Machine 1 spent coffe: " + i1);
			if (300 - i1 < ct.getCoffeeAmount())
				createRefill(getMachObject(1));
		}

		Refill latestRefillMach2 = dBService.getLatestRefillForMachine(getMachObject(2));
		if ((latestRefillMach2 != null && !isRefillInTheFuture(latestRefillMach2)) || latestRefillMach2 == null) {
			long i2 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach2, getMachObject(2));
			logger.info("Machine 2 spent coffe: " + i2);
			if (300 - i2 < ct.getCoffeeAmount())
				createRefill(getMachObject(2));
		}

		Refill latestRefillMach3 = dBService.getLatestRefillForMachine(getMachObject(3));
		if ((latestRefillMach3 != null && !isRefillInTheFuture(latestRefillMach3)) || latestRefillMach3 == null) {
			long i3 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach3, getMachObject(3));
			logger.info("Machine 3 spent coffe: " + i3);
			if (300 - i3 < ct.getCoffeeAmount())
				createRefill(getMachObject(3));
		}
	}

	private void createRefill(Machine mach) {
		Refill r = new Refill();
		r.setMachine(mach);
		CoffeeOrder latestOrderMach = dBService.getLatestOrderForMachine(mach);
		if (isOrderInTheFuture(latestOrderMach))
			r.setTime(latestOrderMach.getTime().plusSeconds(latestOrderMach.getCoffeeType().getPreparationTime()));
		else
			r.setTime(LocalDateTime.now());
		dBService.saveRefill(r);
	}

	private Machine getMachObject(int i) {
		Machine mach = new Machine();
		mach.setId(i);
		mach.setCapacity(300);
		return mach;
	}

}
