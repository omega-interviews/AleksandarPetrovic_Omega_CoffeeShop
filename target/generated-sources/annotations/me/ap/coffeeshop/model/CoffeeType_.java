package me.ap.coffeeshop.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CoffeeType.class)
public abstract class CoffeeType_ {

	public static volatile SingularAttribute<CoffeeType, Boolean> deleted;
	public static volatile SingularAttribute<CoffeeType, Double> price;
	public static volatile SingularAttribute<CoffeeType, String> imagePath;
	public static volatile SingularAttribute<CoffeeType, String> name;
	public static volatile SingularAttribute<CoffeeType, Integer> preparationTime;
	public static volatile SingularAttribute<CoffeeType, Long> id;
	public static volatile SingularAttribute<CoffeeType, Integer> coffeeAmount;

}

