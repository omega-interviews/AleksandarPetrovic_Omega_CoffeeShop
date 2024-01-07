package me.ap.coffeeshop.util;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;

@Stateless
public class UtilBean {

	public FacesMessage errorMsg(String msgText) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msgText);
	}
	
	public FacesMessage warnMsg(String msgText) {
		return new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", msgText);
	}

	public FacesMessage successMsg(String msgText) {
		return new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", msgText);
	}

}
