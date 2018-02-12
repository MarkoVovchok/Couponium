package db.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import exceptions_handling.CouponSystemException;

public class TableCreator {

	public static void main(String[] args) {

		/**
		 * This class is used to create all the tables in the database.
		 */

		File driverFile = new File("files/driverDB");
		File portFile = new File("files/PortAdress");

		// Driver loading section

		try (Scanner sc = new Scanner(driverFile)) {
			String driverName = sc.nextLine();
			Class.forName(driverName);
			System.out.println("Driver loading acomplished");
		} catch (FileNotFoundException | ClassNotFoundException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("Driver loading trouble");
		}

		// Database creation section with Create Tables methods; New Tables can
		// be added to this if needed;

		Connection con = null;

		try (Scanner sc = new Scanner(portFile)) {
			String adress = sc.nextLine();
			con = DriverManager.getConnection(adress);
			System.out.println("Connection established");

			createCompanyTable(con);
			createCouponTable(con);
			createCustomerTable(con);
			createCompanyCouponTable(con);
			createCustomerCouponTable(con);

		} catch (IOException | SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("Table Creator was unable to complete.Check server status and adress");
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					Exception ex = new CouponSystemException(e);
					System.out.println(ex);
					System.out.println("Still connected to the server");
				}
				System.out.println("Disconnected");
			}
		}

	}

	// Static methods to use with TableCreator;

	private static void createCompanyTable(Connection con) {
		String sql = "CREATE TABLE company(id BIGINT PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(50), email VARCHAR(50))";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Company table already exists, or syntax error");
		}
		System.out.println(sql + "creation completed");
	}

	private static void createCustomerTable(Connection con) {
		String sql = "CREATE TABLE customer(id BIGINT PRIMARY KEY, cust_name VARCHAR(50) NOT NULL UNIQUE,password VARCHAR(50))";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Customer table already exists, or syntax error");
		}
		System.out.println(sql + "creation completed");
	}

	private static void createCouponTable(Connection con) {
		String sql = "CREATE TABLE  coupon (id BIGINT PRIMARY KEY, title VARCHAR(50) UNIQUE, start_date DATE, end_date DATE, amount INTEGER, type VARCHAR (50), message VARCHAR(50), price DOUBLE,image VARCHAR(50))";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Coupon table already exists, or syntax error");
		}
		System.out.println(sql + "creation completed");
	}

	private static void createCustomerCouponTable(Connection con) {
		String sql = "CREATE TABLE customer_coupon (customer_id BIGINT, coupon_id BIGINT, PRIMARY KEY(Customer_id,Coupon_id))";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("customer_coupon table already exists, or syntax error");
		}
		System.out.println("creation completed " + sql);
	}

	private static void createCompanyCouponTable(Connection con) {
		String sql = "CREATE TABLE company_coupon (company_id BIGINT, coupon_id BIGINT, PRIMARY KEY(Company_id,Coupon_id))";
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("company_coupon table already exists, or syntax error");
		}
		System.out.println("creation completed " + sql);
	}

}
