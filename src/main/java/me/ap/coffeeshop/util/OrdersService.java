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

	public Long determineMachine(CoffeeOrder order) {

		checkIfRefillsNeeded(order.getCoffeeType());

		// machine 1 busy util latest refill or order
		LocalDateTime mach1busyUntil = null;
		Refill latestRefillMach1 = dBService.getLatestRefillForMachine(getMachObject(1));
		if (latestRefillMach1 != null && isRefillInTheFuture(latestRefillMach1))
			mach1busyUntil = latestRefillMach1.getFinishTime();
		CoffeeOrder latestOrderMach1 = dBService.getLatestOrderForMachine(getMachObject(1));
		if (latestOrderMach1 != null && isOrderInTheFuture(latestOrderMach1))
			if (mach1busyUntil == null || latestOrderMach1.getFinishTime().compareTo(mach1busyUntil) > 0)
				mach1busyUntil = latestOrderMach1.getFinishTime();

		// if machine 1 is free, place order
		if (mach1busyUntil == null)
			return placeOrderToMachine(order, getMachObject(1), null);

		// machine 2 busy util latest refill or order
		LocalDateTime mach2busyUntil = null;
		Refill latestRefillMach2 = dBService.getLatestRefillForMachine(getMachObject(2));
		if (latestRefillMach2 != null && isRefillInTheFuture(latestRefillMach2))
			mach2busyUntil = latestRefillMach2.getFinishTime();
		CoffeeOrder latestOrderMach2 = dBService.getLatestOrderForMachine(getMachObject(2));
		if (latestOrderMach2 != null && isOrderInTheFuture(latestOrderMach2))
			if (mach2busyUntil == null || latestOrderMach2.getFinishTime().compareTo(mach2busyUntil) > 0)
				mach2busyUntil = latestOrderMach2.getFinishTime();

		// if machine 2 is free, place order
		if (mach2busyUntil == null)
			return placeOrderToMachine(order, getMachObject(2), null);

		// machine 3 busy util latest refill or order
		LocalDateTime mach3busyUntil = null;
		Refill latestRefillMach3 = dBService.getLatestRefillForMachine(getMachObject(3));
		if (latestRefillMach3 != null && isRefillInTheFuture(latestRefillMach3))
			mach3busyUntil = latestRefillMach3.getFinishTime();
		CoffeeOrder latestOrderMach3 = dBService.getLatestOrderForMachine(getMachObject(3));
		if (latestOrderMach3 != null && isOrderInTheFuture(latestOrderMach3))
			if (mach3busyUntil == null || latestOrderMach3.getFinishTime().compareTo(mach3busyUntil) > 0)
				mach3busyUntil = latestOrderMach3.getFinishTime();

		// if machine 3 is free, place order
		if (mach3busyUntil == null)
			return placeOrderToMachine(order, getMachObject(3), null);

		// if all 3 are busy, find the earliest free one
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

	private Long placeOrderToMachine(CoffeeOrder o, Machine m, LocalDateTime t) {
		o.setMachine(m);
		if (t != null) {
			o.setTime(t);
			o.setFinishTime(t.plusSeconds(o.getCoffeeType().getPreparationTime()));
		} else {
			o.setTime(LocalDateTime.now());
			o.setFinishTime(LocalDateTime.now().plusSeconds(o.getCoffeeType().getPreparationTime()));
		}
		CoffeeOrder result = dBService.saveOrder(o);
		return result.getId();
	}

	public boolean isRefillInTheFuture(Refill refill) {
		LocalDateTime now = LocalDateTime.now();
		if (refill.getFinishTime().compareTo(now) > 0)
			return true;
		else
			return false;
	}

	public boolean isOrderInTheFuture(CoffeeOrder order) {
		LocalDateTime now = LocalDateTime.now();
		if (order.getFinishTime().compareTo(now) > 0)
			return true;
		else
			return false;
	}

	private void checkIfRefillsNeeded(CoffeeType ct) {

		Refill latestRefillMach1 = dBService.getLatestRefillForMachine(getMachObject(1));
		long i1 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach1, getMachObject(1));
		logger.info("Machine 1 spent coffe since last refill: " + i1);
		if (300 - i1 < ct.getCoffeeAmount())
			createRefill(getMachObject(1));

		Refill latestRefillMach2 = dBService.getLatestRefillForMachine(getMachObject(2));
		long i2 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach2, getMachObject(2));
		logger.info("Machine 2 spent coffe since last refill: " + i2);
		if (300 - i2 < ct.getCoffeeAmount())
			createRefill(getMachObject(2));

		Refill latestRefillMach3 = dBService.getLatestRefillForMachine(getMachObject(3));
		long i3 = dBService.getOrdersSinceLastRefillForMachine(latestRefillMach3, getMachObject(3));
		logger.info("Machine 3 spent coffe since last refill: " + i3);
		if (300 - i3 < ct.getCoffeeAmount())
			createRefill(getMachObject(3));
	}

	private void createRefill(Machine mach) {
		Refill r = new Refill();
		r.setMachine(mach);
		CoffeeOrder latestOrderMach = dBService.getLatestOrderForMachine(mach);
		if (isOrderInTheFuture(latestOrderMach)) {
			r.setTime(latestOrderMach.getFinishTime());
			r.setFinishTime(r.getTime().plusSeconds(120));
		} else {
			r.setTime(LocalDateTime.now());
			r.setFinishTime(r.getTime().plusSeconds(120));
		}
		dBService.saveRefill(r);
	}

	private Machine getMachObject(int i) {
		Machine mach = new Machine();
		mach.setId(i);
		mach.setCapacity(300);
		return mach;
	}
	
	public static void main(String[] args) {
		int x = 3;
		if (x == 1)
			if (x == 2)
				System.out.println(x);
	}

}
