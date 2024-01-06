package me.ap.coffeeshop.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.logging.Logger;

import me.ap.coffeeshop.model.CoffeeOrder;
import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.model.Refill;

@Stateless
public class DBService {

	@Inject
	private Logger logger;
	@Inject
	private EntityManager em;

	public Machine loadMachineByNum(long num) {
		return em.createQuery("select m from Machine m where m.id = :num", Machine.class).setParameter("num", num)
				.getSingleResult();
	}

	public Refill getLatestRefillForMachine(Machine mach) {
		List<Refill> result = em
				.createQuery("select r from Refill r where r.machine = :mach order by r.time desc", Refill.class)
				.setParameter("mach", mach).getResultList();
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	public CoffeeOrder getLatestOrderForMachine(Machine mach) {
		List<CoffeeOrder> result = em
				.createQuery("select o from CoffeeOrder o where o.machine = :mach order by o.time desc",
						CoffeeOrder.class)
				.setParameter("mach", mach).getResultList();
		if (result.size() > 0) {
			logger.info("Latest order for machine " + mach.getId() + ": " + result.get(0));
			return result.get(0);
		} else
			return null;
	}

	public List<CoffeeType> getAllActiveCoffeeTypes() {
		return em.createQuery("select c from CoffeeType c where c.deleted = false", CoffeeType.class).getResultList();
	}

	public long getOrdersSinceLastRefillForMachine(Refill refill, Machine machine) {
		List<CoffeeOrder> list = new ArrayList<>();
		if (refill != null) {
			list = em
					.createQuery("select o from CoffeeOrder o where o.machine = :mach and o.time > :t",
							CoffeeOrder.class)
					.setParameter("mach", machine).setParameter("t", refill.getTime()).getResultList();
		} else {
			list = em.createQuery("select o from CoffeeOrder o where o.machine = :mach", CoffeeOrder.class)
					.setParameter("mach", machine).getResultList();
		}
		int totalAmount = 0;
		for (CoffeeOrder coffeeOrder : list) {
			totalAmount += coffeeOrder.getCoffeeType().getCoffeeAmount();
		}
		return totalAmount;
//		if (refill != null)
//			return em.createQuery(
//					"select sum(o.coffeeType.coffeeAmount) from CoffeeOrder o where o.machine = :mach and o.time > :t",
//					Long.class).setParameter("mach", refill.getMachine()).setParameter("t", refill.getTime())
//					.getSingleResult();
//		else
//			return em.createQuery("select sum(o.coffeeType.coffeeAmount) from CoffeeOrder o where o.machine = :mach",
//					Long.class).setParameter("mach", machine).getSingleResult();
	}

	public List<CoffeeOrder> getCurrentCoffeeToGoOrders() {
		int longestPrepTime = em
				.createQuery("select max(ct.preparationTime) from CoffeeType ct where ct.deleted = false",
						Integer.class)
				.getSingleResult();
		LocalDateTime t = LocalDateTime.now();
		t = t.minusSeconds(longestPrepTime);
		List<CoffeeOrder> result = em
				.createQuery("select o from CoffeeOrder o where o.machine is null and o.time > :t", CoffeeOrder.class)
				.setParameter("t", t).getResultList();
		return result;
	}

	public List<CoffeeOrder> getCurrentCoffeeOrdersForMachine(Machine mach) {
		int longestPrepTime = em
				.createQuery("select max(ct.preparationTime) from CoffeeType ct where ct.deleted = false",
						Integer.class)
				.getSingleResult();
		LocalDateTime t = LocalDateTime.now();
		t = t.minusSeconds(longestPrepTime);
		List<CoffeeOrder> result = em
				.createQuery("select o from CoffeeOrder o where o.machine = :mach and o.time > :t", CoffeeOrder.class)
				.setParameter("mach", mach).setParameter("t", t).getResultList();
		return result;
	}

	public CoffeeType getCoffeeTypeById(long id) {
		try {
			return em.createQuery("select ct from CoffeeType ct where ct.id = :id and ct.deleted = false", CoffeeType.class)
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	public CoffeeType saveCoffeeType(CoffeeType ct) {
		logger.info("Saving new coffee type: " + ct);
		return em.merge(ct);
	}

	public CoffeeOrder saveOrder(CoffeeOrder o) {
		logger.info("Saving new order: " + o);
		return em.merge(o);
	}

	public Refill saveRefill(Refill r) {
		logger.info("Saving new refill: " + r);
		return em.merge(r);
	}

}
