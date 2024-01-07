package me.ap.coffeeshop.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import me.ap.coffeeshop.model.CoffeeType;
import me.ap.coffeeshop.util.DBService;

@Path("/admin/coffeeTypes")
@RequestScoped
public class AdminRestService {

	@Inject
	private Logger logger;
	@Inject
	private DBService dBService;

	@Path("/edit")
	@POST
	public Response editCoffeeType(@QueryParam(value = "id") Long id, @QueryParam(value = "name") String name,
			@QueryParam(value = "preparationTime") Integer preparationTime,
			@QueryParam(value = "coffeeAmount") Integer coffeeAmount, @QueryParam(value = "price") Double price) {

		logger.info("Called edit CoffeType endpoint. Params id=" + id + " name=" + name + " preparationTime="
				+ preparationTime + " coffeeAmount=" + coffeeAmount + " price=" + price);
		Response.ResponseBuilder builder = null;
		if (id == null) {
			builder = Response.accepted("ID is a required parameter for this action.");
			return builder.build();
		}

		CoffeeType ct = dBService.getCoffeeTypeById(id);
		if (ct == null) {
			builder = Response.accepted("Coffee type with that ID doesn't exist.");
			return builder.build();
		}

		if (name != null)
			ct.setName(name);
		if (preparationTime != null)
			ct.setPreparationTime(preparationTime);
		if (coffeeAmount != null)
			ct.setCoffeeAmount(coffeeAmount);
		if (price != null)
			ct.setPrice(price);

		dBService.saveCoffeeType(ct);

		builder = Response.ok();
		return builder.build();
	}

	@Path("/add")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addCoffeeType(@QueryParam(value = "name") String name,
			@QueryParam(value = "preparationTime") Integer preparationTime,
			@QueryParam(value = "coffeeAmount") Integer coffeeAmount, @QueryParam(value = "price") Double price) {

		logger.info("Called edit CoffeType endpoint. Params name=" + name + " preparationTime=" + preparationTime
				+ " coffeeAmount=" + coffeeAmount + " price=" + price);
		Response.ResponseBuilder builder = null;

		if (name == null) {
			builder = Response.accepted("The required parameter <name> is null.");
			return builder.build();
		}
		if (preparationTime == null) {
			builder = Response.accepted("The required parameter <preparationTime> is null.");
			return builder.build();
		}
		if (coffeeAmount == null) {
			builder = Response.accepted("The required parameter <coffeeAmount> is null.");
			return builder.build();
		}
		if (price == null) {
			builder = Response.accepted("The required parameter <price> is null.");
			return builder.build();
		}

		CoffeeType ct = new CoffeeType();
		ct.setName(name);
		ct.setPreparationTime(preparationTime);
		ct.setCoffeeAmount(coffeeAmount);
		ct.setPrice(price);

		dBService.saveCoffeeType(ct);

		builder = Response.ok();
		return builder.build();
	}

	@Path("/delete/{id}")
	@POST
	public Response deleteCoffeeType(@PathParam(value = "id") Long id) {

		logger.info("Called deleted coffee type endpoint, id param " + id);
		Response.ResponseBuilder builder = null;
		if (id == null) {
			builder = Response.accepted("ID is a required parameter for this action.");
			return builder.build();
		}

		CoffeeType ct = dBService.getCoffeeTypeById(id);
		if (ct == null) {
			builder = Response.accepted("Coffee type with that ID doesn't exist.");
			return builder.build();
		}

		ct.setDeleted(true);
		ct = dBService.saveCoffeeType(ct);

		builder = Response.ok();
		return builder.build();
	}

	@Path("/image/{id}")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response editImage(@PathParam(value = "id") Long id, MultipartFormDataInput input) {

		logger.info("Called image for coffee type endpoint, id param " + id);
		Response.ResponseBuilder builder = null;
		if (id == null) {
			builder = Response.accepted("ID is a required parameter for this action.");
			return builder.build();
		}

		CoffeeType ct = dBService.getCoffeeTypeById(id);
		if (ct == null) {
			builder = Response.accepted("Coffee type with that ID doesn't exist.");
			return builder.build();
		}

		if (input == null) {
			builder = Response.accepted("Image is a required parameter.");
			return builder.build();
		}

		StringBuilder filename = new StringBuilder();
		StringBuilder extension = new StringBuilder();
		parseFileNameFromData(input, filename, extension);
		if (filename.length() == 0 || extension.length() == 0) {
			builder = Response.accepted("Sorry, we couldn't extract name and extension for the image.");
			return builder.build();
		}

		byte[] bytes;
		try {
			bytes = input.getFormDataPart("imageFile", byte[].class, null);
			String filePath = handleFileUpload(new ByteArrayInputStream(bytes), filename.toString(),
					extension.toString());
			if (!filePath.isEmpty()) {
				ct.setImagePath(filePath);
				dBService.saveCoffeeType(ct);
				builder = Response.ok();
				return builder.build();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}

	@Path("/active")
	@GET
	@Produces("application/json")
	public Response allActiveCoffeeTypes() {
		logger.info("Called all active coffee types endpoint");
		List<CoffeeType> list = dBService.getAllActiveCoffeeTypes();
		JSONArray jsonarray = new JSONArray();
		for (CoffeeType coffeeType : list) {
			JSONObject jsonobject = new JSONObject();
			jsonobject.put("id", coffeeType.getId());
			jsonobject.put("name", coffeeType.getName());
			jsonobject.put("preparationTime", coffeeType.getPreparationTime());
			jsonobject.put("coffeeAmount", coffeeType.getCoffeeAmount());
			jsonobject.put("price", coffeeType.getPrice());
			logger.info(jsonobject);
		}
		Response.ResponseBuilder builder = null;
		builder = Response.ok(jsonarray.toString());
		return builder.build();
	}

	public String handleFileUpload(InputStream is, String filename, String extension) {
		try {
			System.out.println("Handle file updload..");
			logger.info("Working Directory = " + System.getProperty("user.dir"));
			java.nio.file.Path folder = Paths.get(System.getProperty("user.dir"));
			java.nio.file.Path temp = Files.createTempFile(folder, filename + "-", "." + extension);
			logger.info("Temp file created: " + temp);
			Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
			String filePath = temp.toString();
			logger.info("File copied to location: " + filePath);
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void parseFileNameFromData(MultipartFormDataInput input, StringBuilder filename, StringBuilder extension) {
		logger.info("Extracting file name from req header..");
		Map<String, List<InputPart>> formParts = input.getFormDataMap();
		String contentdisp = formParts.get("imageFile").get(0).getHeaders().get("Content-Disposition").get(0);
		logger.info("Content-disposition string=" + contentdisp);
		int dotIndex = contentdisp.indexOf("filename=");
		logger.info("Index of filename= " + dotIndex);
		filename.append(contentdisp.substring(dotIndex + 10, contentdisp.length() - 1));
		logger.info("File name with ext: " + filename);
		dotIndex = filename.indexOf(".");
		logger.info("Index of dot: " + dotIndex);
		extension.append(filename.substring(dotIndex, filename.length()));
		logger.info("Extension: " + extension);
		filename.substring(0, dotIndex);
		logger.info("File name: " + filename);
	}

}
