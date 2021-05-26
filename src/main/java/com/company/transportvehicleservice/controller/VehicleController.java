package com.company.transportvehicleservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.company.transportvehicleservice.configs.MapperConfig;
import com.company.transportvehicleservice.model.Request;
import com.company.transportvehicleservice.model.Vehicle;
import com.company.transportvehicleservice.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleController {

	private static final Logger LOGGER = Logger.getLogger(VehicleController.class);
	private static ObjectMapper mapper = MapperConfig.getMapper();
	private static VehicleService vehicleService = VehicleService.getVehicleService();

	@GET
	public Response getVehiclesList() {
		LOGGER.info("Request received for getAllVehiclesList");
		String response = "";
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			vehicles = vehicleService.getVehicleList();
			response = mapper.writeValueAsString(vehicles);
			LOGGER.info("Request Processed Successfull");
			LOGGER.info("Response ::" + response);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<String, String>();
			errorResponse.put("status", "Error");
			errorResponse.put("message", e.getMessage());
			LOGGER.error("Exception occured :: " + e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
		}
		return Response.status(Status.OK).entity(response).build();
	}

	@GET
	@Path("/route")
	public Response getVehicleByRoute(String payload) {
		LOGGER.info("Request received for getting vehicle By route" + payload);
		Request request = new Request();
		String response = "";
		try {
			request = mapper.readValue(payload, Request.class);
			response = mapper.writeValueAsString(vehicleService.getVehiclesByRoute(request));
			LOGGER.info("Request Processed Successfull");
			LOGGER.info("Response ::" + response);
		} catch (Exception exception) {
			Map<String, String> errorResponse = new HashMap<String, String>();
			errorResponse.put("status", "Error");
			errorResponse.put("message", exception.getMessage());
			LOGGER.error("Error response :: " + errorResponse);
			return Response.status(Status.OK).entity(errorResponse).build();
		}

		return Response.status(Status.OK).entity(response).build();

	}

	@GET
	@Path("/seat")
	public Response getAvailableSeat(String payload) {
		LOGGER.info("Request received for getting available seat in vehicle" + payload);
		Request request = new Request();
		String response = "";
		try {
			request = mapper.readValue(payload, Request.class);
			response = mapper.writeValueAsString(vehicleService.getAvailableSeat(request));
			LOGGER.info("Request Processed Successfull");
			LOGGER.info("Response ::" + response);
		} catch (Exception exception) {
			Map<String, String> errorResponse = new HashMap<String, String>();
			errorResponse.put("status", "Error");
			errorResponse.put("message", exception.getMessage());
			LOGGER.error("Error response :: " + errorResponse);
			return Response.status(Status.OK).entity(errorResponse).build();
		}

		return Response.status(Status.OK).entity(response).build();

	}

}
