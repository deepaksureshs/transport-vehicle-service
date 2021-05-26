package com.company.transportvehicleservice.dao;

import java.sql.Date;
import java.util.List;

import com.company.transportvehicleservice.model.Vehicle;
import com.company.transportvehicleservice.model.VehicleRouteMap;

public interface VehicleDAO {

	public List<Vehicle> getVehicleList() throws Exception;

	public List<Vehicle> getVehicleByRoute(int routeId, Date date) throws Exception;

	public VehicleRouteMap getAvailableSeat(int routeId, int vehicleId, Date date) throws Exception;
}
