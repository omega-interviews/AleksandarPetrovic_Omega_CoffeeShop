package me.ap.coffeeshop.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resource {
	
	@Produces
    @PersistenceContext
    private EntityManager em;
	
	@Produces
	public org.jboss.logging.Logger produceLogger(InjectionPoint p) {
		return org.jboss.logging.Logger.getLogger(p.getClass().getCanonicalName());
	}
	
	@Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
