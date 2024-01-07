package me.ap.coffeeshop.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CoffeeOrder.class)
public abstract class CoffeeOrder_ {

	public static volatile SingularAttribute<CoffeeOrder, CoffeeType> coffeeType;
	public static volatile SingularAttribute<CoffeeOrder, Machine> machine;
	public static volatile SingularAttribute<CoffeeOrder, Long> id;
	public static volatile SingularAttribute<CoffeeOrder, LocalDateTime> time;
	public static volatile SingularAttribute<CoffeeOrder, Integer> tableNumber;

}

