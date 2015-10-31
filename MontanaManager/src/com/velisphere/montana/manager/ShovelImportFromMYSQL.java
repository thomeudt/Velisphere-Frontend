package com.velisphere.montana.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.voltdb.client.*;

public class ShovelImportFromMYSQL {

	private static Connection conn;

	public static void main(String[] args) throws Exception {

		/*
		 * Instantiate a client and connect to the database.
		 */

		// VoltDB

		System.out.println("Connecting to VoltDB...");

		org.voltdb.client.Client voltShovel;
		voltShovel = ClientFactory.createClient();
		voltShovel.createConnection("192.168.1.101");

		// MySQL

		connect();

		Statement mySelect = conn.createStatement();

		//-----------------------------------------------------
		
		try {

			System.out.println("Submitting Query to MySQL...");

			ResultSet myResult = mySelect
					.executeQuery("SELECT * FROM USER");

			while (myResult.next()) {
					// extract the value in column checkid

					System.out.println("Adding item to VoltDB...");

					try {
						voltShovel.callProcedure("USER.insert", myResult.getString(1),
								myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6));
					} catch (NoConnectionsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProcCallException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
				
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//-----------------------------------------------------
		
		try {

			System.out.println("Submitting Query to MySQL...");

			ResultSet myResult = mySelect
					.executeQuery("SELECT * FROM ACTION");

			while (myResult.next()) {
					// extract the value in column checkid

					System.out.println("Adding item to VoltDB...");

					try {
						voltShovel.callProcedure("ACTION.insert", myResult.getString(1),
								myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getString(8));
					} catch (NoConnectionsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProcCallException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
				
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//-----------------------------------------------------
		
		try {

			System.out.println("Submitting Query to MySQL...");

			ResultSet myResult = mySelect
					.executeQuery("SELECT * FROM ALERT");

			while (myResult.next()) {
					// extract the value in column checkid

					System.out.println("Adding item to VoltDB...");

					try {
						voltShovel.callProcedure("ALERT.insert", myResult.getString(1),
								myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getString(8), myResult.getString(9), myResult.getString(10), myResult.getString(11));
					} catch (NoConnectionsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProcCallException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
				
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


				
		//-----------------------------------------------------
		
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM CHECKPATH");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("CHECKPATH.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM CHECKPATH_CHECK_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("CHECKPATH_CHECK_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM CHECKPATH_MULTICHECK_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("CHECKPATH_MULTICHECK_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
	//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM CHECKSTATE");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("CHECKSTATE.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM CHECKTABLE");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("CHECK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getString(8), myResult.getString(9));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM ENDPOINT");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("ENDPOINT.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
	//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM ENDPOINTCLASS");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("ENDPOINTCLASS.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM ENDPOINT_SPHERE_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("ENDPOINT_SPHERE_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM ENDPOINT_USER_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("ENDPOINT_USER_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

				

				//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM FAVORITES");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("FAVORITES.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM ITEMCOST");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("ITEMCOST.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM LOGICTEMPLATE");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("LOGICTEMPLATE.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM LOGICTEMPLATE_ENDPOINTCLASS_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("LOGICTEMPLATE_ENDPOINTCLASS_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM MULTICHECK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("MULTICHECK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM MULTICHECK_CHECK_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("MULTICHECK_CHECK_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM MULTICHECK_MULTICHECK_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("MULTICHECK_MULTICHECK_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
		//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM OUTBOUNDPROPERTYACTION");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("OUTBOUNDPROPERTYACTION.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getString(8));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM PLAN");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("PLAN.insert", myResult.getString(1),
										myResult.getString(2));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM PROPERTY");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("PROPERTY.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5), myResult.getString(6), myResult.getString(7), myResult.getString(8));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM PROPERTYCLASS");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("PROPERTYCLASS.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM SPHERE");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("SPHERE.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM SPHERE_USER_LINK");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("SPHERE_USER_LINK.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM UNPROVISIONED_ENDPOINT");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("UNPROVISIONED_ENDPOINT.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3), myResult.getString(4), myResult.getString(5));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//-----------------------------------------------------
				
				try {

					System.out.println("Submitting Query to MySQL...");

					ResultSet myResult = mySelect
							.executeQuery("SELECT * FROM VENDOR");

					while (myResult.next()) {
							// extract the value in column checkid

							System.out.println("Adding item to VoltDB...");

							try {
								voltShovel.callProcedure("VENDOR.insert", myResult.getString(1),
										myResult.getString(2), myResult.getString(3));
							} catch (NoConnectionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ProcCallException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							
						
					}

					// System.out.println(allEndPoints);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
		System.out.println("Closing MySQL...");

		mySelect.close();

		System.out.println("Closing VoltDB...");

		voltShovel.close();

		System.out.println("Done!");

		/*
		 * Retrieve the message.
		 */

	}

	/* This example connects to Vertica and issues a simple select */

	public static void connect() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = null;
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find the JDBC driver class.\n");
			e.printStackTrace();
			return;
		}
		try {

			System.out.println("Connecting to mySQL...");

			conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/montana_backup", "shovel",
					"lorenz");

			conn.setAutoCommit(true);

		} catch (SQLException e) {
			System.err.println("Could not connect to the database.\n");
			e.printStackTrace();
			return;
		}
	}

}
