package me.ap.coffeeshop.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.model.Machine;
import me.ap.coffeeshop.util.DBService;

@Named
@ViewScoped
public class AdminView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;

	private List<CoffeeType> coffeeTypes;
	private Machine machine1;
	private Machine machine2;
	private Machine machine3;

	private CoffeeType coffeeType = new CoffeeType();

	private UploadedFile file;
	private String filePath;

	@PostConstruct
	private void init() {
		setMachine1(dBService.loadMachineByNum(1));
		setMachine2(dBService.loadMachineByNum(2));
		setMachine3(dBService.loadMachineByNum(3));
		setCoffeeTypes(dBService.getAllActiveCoffeeTypes());
		logger.info("Loaded coffee types: " + coffeeTypes.size());
	}

	public void loadCoffeType(CoffeeType ct) {
		coffeeType = ct;
	}

	public void saveCoffeeType() {
		coffeeType.setImagePath(filePath);
		dBService.saveCoffeeType(coffeeType);
		coffeeType = new CoffeeType();
		setCoffeeTypes(dBService.getAllActiveCoffeeTypes());
		FacesMessage message = new FacesMessage("Coffee type sucessfully saved.");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void deleteCoffeeType(CoffeeType ct) {
		ct.setDeleted(true);
		ct = dBService.saveCoffeeType(ct);
		logger.info("Coffee type id " + ct.getId() + " successfully deleted.");
		setCoffeeTypes(dBService.getAllActiveCoffeeTypes());
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        try {
        	file = event.getFile();
        	System.out.println("Handle file updload..");
            FacesMessage msg = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            logger.info("Working Directory = " + System.getProperty("user.dir"));
			Path folder = Paths.get(System.getProperty("user.dir"));
			String filename = FilenameUtils.getBaseName(file.getFileName());
			logger.info("Filename: " + filename);
			String extension = FilenameUtils.getExtension(file.getFileName());
			logger.info("Extension: " + extension);
			Path temp = Files.createTempFile(folder, filename + "-", "." + extension);
			logger.info("Temp file created: " + temp);
			InputStream input = file.getInputStream();
			Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
			filePath = temp.toString();
			logger.info("File copied to location: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public List<CoffeeType> getCoffeeTypes() {
		return coffeeTypes;
	}

	public void setCoffeeTypes(List<CoffeeType> coffeeTypes) {
		this.coffeeTypes = coffeeTypes;
	}

	public CoffeeType getCoffeeType() {
		return coffeeType;
	}

	public void setCoffeeType(CoffeeType coffeeType) {
		this.coffeeType = coffeeType;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

	}
	
}
