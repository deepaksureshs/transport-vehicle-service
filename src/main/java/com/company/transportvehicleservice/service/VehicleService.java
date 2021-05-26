package com.company.transportvehicleservice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.company.transportvehicleservice.dao.VehicleDAO;
import com.company.transportvehicleservice.dao.VehicleDaoImplementation;
import com.company.transportvehicleservice.model.Request;
import com.company.transportvehicleservice.model.Vehicle;
import com.company.transportvehicleservice.model.VehicleRouteMap;

public class VehicleService {

	private static final Logger LOGGER = Logger.getLogger(VehicleService.class);
	private static VehicleService vehicleService = null;
	private static VehicleDAO dao;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private VehicleService() {

	}

	public static VehicleService getVehicleService() {
		if (vehicleService == null) {
			vehicleService = new VehicleService();
		}
		return vehicleService;
	}

	public List<Vehicle> getVehicleList() throws Exception {
		dao = new VehicleDaoImplementation();
		List<Vehicle> vehicles = dao.getVehicleList();
		LOGGER.info("Vehicle List :: " + vehicles);
		return vehicles;
	}

	public List<Vehicle> getVehiclesByRoute(Request request) throws Exception {
		Date date = null;

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		dao = new VehicleDaoImplementation();
		try {
			date = format.parse(request.getRouteDate());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			vehicles = dao.getVehicleByRoute(request.getRoute().getRouteId(), sqlDate);
			LOGGER.info("Vehicle List :: " + vehicles);
		} catch (ParseException e) {
			LOGGER.error("ParseException :: " + e);
			throw new Exception("recived incorrect date format, verify and try again");
		} catch (NullPointerException e) {
			LOGGER.error("NullPointerException :: " + e);
			throw new Exception("[ticket, vehicle , route] details are mandatory, verify and try again");
		} catch (Exception e) {
			LOGGER.error("Exception while geting vehicle by route :: " + e);
			throw new Exception(e.getMessage());
		}
		return vehicles;
	}

	public VehicleRouteMap getAvailableSeat(Request request) throws Exception {
		VehicleRouteMap vehicleRouteMap = new VehicleRouteMap();
		Date date = null;
		dao = new VehicleDaoImplementation();
		try {
			date = format.parse(request.getRouteDate());
			vehicleRouteMap = dao.getAvailableSeat(request.getRoute().getRouteId(), request.getVehicle().getVehicleId(),
					new java.sql.Date(date.getTime()));
			LOGGER.info("Vehicle Route details :: " + vehicleRouteMap);
		} catch (ParseException e) {
			LOGGER.error("ParseException :: " + e);
			throw new Exception("recived incorrect date format, verify and try again");
		} catch (NullPointerException e) {
			LOGGER.error("NullPointerException :: " + e);
			throw new Exception("[ticket, vehicle , route] details are mandatory, verify and try again");
		} catch (Exception e) {
			LOGGER.error("Exception while checking seat available:: " + e);
			throw new Exception(e.getMessage());
		}

		return vehicleRouteMap;

	}

}
