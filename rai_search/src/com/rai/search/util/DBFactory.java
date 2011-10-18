package com.rai.search.util;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public final class DBFactory {

	private static Mongo mongo = null;
	private static DB db = null;
	private static String address = "localhost";
	private static String port = "27017";
	private static String database = "myDB";

	public static DB getInstance() throws UnknownHostException, MongoException {

		if (mongo == null) {
			mongo = new Mongo(address, Integer.valueOf(port));

			db = mongo.getDB(database);
		}

		return db;
	}

	public static void init(String l_address, String l_port, String l_database) {
		address = l_address;
		port = l_port;
		database = l_database;
	}
}
