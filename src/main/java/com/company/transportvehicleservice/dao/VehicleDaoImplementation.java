package com.company.transportvehicleservice.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.company.transportvehicleservice.configs.DbConfig;
import com.company.transportvehicleservice.model.User;
import com.company.transportvehicleservice.model.Vehicle;
import com.company.transportvehicleservice.model.VehicleRouteMap;

public class VehicleDaoImplementation implements VehicleDAO {

	private static Connection dbConnection;
	private static final Logger LOGGER = Logger.getLogger(VehicleDaoImplementation.class);

	public VehicleDaoImplementation() throws Exception {
		if (dbConnection == null) {
			dbConnection = DbConfig.getConnection();
		}
	}

	public List<Vehicle> getVehicleList() throws Exception {

		PreparedStatement statement;
		List<Vehicle> vehiclList = new ArrayList<Vehicle>();
		try {
			LOGGER.info("Getting vehicle list from db :: ");
			statement = dbConnection.prepareStatement(
					"SELECT vehicle_id,vehicle_name,registration_number,vehicle_type,capacity,health_status,created_date,updated_date,created_by,updated_by FROM VEHICLE");
			LOGGER.info("PreparedStatement :: " + statement);
			ResultSet resultSet = statement.executeQuery();
			extractVehicleList(vehiclList, resultSet);
		} catch (SQLException e) {
			LOGGER.info("SQLException occured :: " + e);
			throw new Exception("Failed to fetch VehicleList by route");
		}
		LOGGER.info("Vehicle List :: " + vehiclList);
		return vehiclList;

	}

	private void extractVehicleList(List<Vehicle> vehiclList, ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			Vehicle vehicle = new Vehicle();
			vehicle.setVehicleId(resultSet.getInt(1));
			vehicle.setVehicleName(resultSet.getString(2));
			vehicle.setRegistrationNumber(resultSet.getString(3));
			vehicle.setVehicleType(resultSet.getString(4));
			vehicle.setCapacity(resultSet.getInt(5));
			vehicle.setHealthStatus(resultSet.getString(6));
			vehicle.setCreatedDate(resultSet.getTimestamp(7).toString());
			vehicle.setUpdatedDate(resultSet.getTimestamp(8).toString());
			vehicle.setCreatedBy(new User(resultSet.getString(9)));
			vehicle.setUpdatedBy(new User(resultSet.getString(10)));
			vehiclList.add(vehicle);
		}
	}

	public List<Vehicle> getVehicleByRoute(int routeId, Date rouetDate) throws Exception {

		List<Vehicle> vehiclList = new ArrayList<Vehicle>();
		try {
			LOGGER.info("Getting vehicle whith route id : " + routeId + " and routeDate :" + rouetDate);
			PreparedStatement statement = dbConnection.prepareStatement(
					"SELECT v.vehicle_id,v.vehicle_name,v.registration_number,v.vehicle_type,v.capacity,v.health_status,v.created_date,v.updated_date,v.created_by,v.updated_by\r\n"
							+ "FROM VEHICLE_ROUTE_MAP vrm  INNER JOIN VEHICLE v  on vrm.vehicle_id=v.vehicle_id where vrm.route_date = ? AND vrm.route_id=? ");

			statement.setDate(1, rouetDate);
			statement.setInt(2, routeId);
			LOGGER.info("PreparedStatement :: " + statement);
			ResultSet rs = statement.executeQuery();
			extractVehicleList(vehiclList, rs);

		} catch (SQLException e) {
			LOGGER.error("SQLException ::" + e);
			throw new Exception("Failed to fetch VehicleList by route");
		}

		return vehiclList;
	}

	public VehicleRouteMap getAvailableSeat(int routeId, int vehicleId, Date date) throws Exception {

		VehicleRouteMap vehicleRouteMap = new VehicleRouteMap();
		try {
			LOGGER.info("Getting available seat with vehicle id " + vehicleId + " route id : " + routeId
					+ " and routeDate :" + date);
			PreparedStatement statement = dbConnection
					.prepareStatement("SELECT  vehicle_route_id,seat_available FROM VEHICLE_ROUTE_MAP \r\n"
							+ "WHERE route_date = ? AND route_id=? AND vehicle_id=?");
			statement.setDate(1, date);
			statement.setInt(2, routeId);
			statement.setInt(3, vehicleId);
			LOGGER.info("PreparedStatement :: " + statement);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				vehicleRouteMap.setVehicleRouteId(rs.getInt(1));
				vehicleRouteMap.setSeatAvailable(rs.getInt(2));
			}
		} catch (SQLException e) {
			LOGGER.error("SQLException ::" + e);
			throw new Exception("Failed to get available seat");
		}
		return vehicleRouteMap;
	}

}
