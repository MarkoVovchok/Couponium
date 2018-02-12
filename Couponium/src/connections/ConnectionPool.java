package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import exceptions_handling.CouponSystemException;

public class ConnectionPool {

	/**
	 * This class is a singleton providing connections by giving access to
	 * connections pool.
	 */

	private Collection<Connection> connections = new HashSet<>();
	private String driver = "org.apache.derby.jdbc.ClientDriver";
	private String portUrl = "jdbc:derby://localhost:1527/couponiumDB";

	private static ConnectionPool instance = null;

	private ConnectionPool() {

		// loading the drivers and handling exceptions inside of the CTOR;
		try {
			Class.forName(driver);
			for (int i = 0; i < 10; i++) {
				Connection con = DriverManager.getConnection(portUrl);
				connections.add(con);
			}

		} catch (ClassNotFoundException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.err.println("Driver loading error");
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.err.println("Connection is unavailable, check server status");
		}
		System.out.println("Driver loaded, connection pool is ready");
	}

	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
			System.out.println("Connection Pool is ready to work");
		}
		return instance;
	}

	/**
	 * This method checks the set of connections for any available and if found
	 * - provides one for use, and clears it from the connections list.
	 */
	public synchronized Connection getConnection() {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Exception ex = new CouponSystemException(e);
				System.out.println(ex);
				System.out.println("Connection interrupted");
			}
		}
		Iterator<Connection> it = connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}

	/**
	 * This method adds an unused connection back to  connection pool, and notifies threads which are on hold
	 */
	public synchronized void returnConnection(Connection con) {
		connections.add(con);
		notify();
	}

	public void closeAllConnections(){
		for (Connection curr : connections) {
			try {
				curr.close();
			} catch (SQLException e) {
				Exception ex = new CouponSystemException(e);
				System.out.println(ex);
				System.err.println("Connection closing failed");
			}
		}
	}

}
