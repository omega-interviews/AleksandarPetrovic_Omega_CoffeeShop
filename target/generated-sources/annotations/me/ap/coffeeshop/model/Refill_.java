package me.ap.coffeeshop.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Refill.class)
public abstract class Refill_ {

	public static volatile SingularAttribute<Refill, Machine> machine;
	public static volatile SingularAttribute<Refill, Long> id;
	public static volatile SingularAttribute<Refill, LocalDateTime> time;

}

