package com.rai.search.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			DB db = DBFactory.getInstance();

			DBCollection word = db.getCollection("word");
			DBCollection doc = db.getCollection("document");
			DBCollection mapper = db.getCollection("word_mapper");
			DBCollection temp = db.getCollection("temp");

			long start = System.currentTimeMillis();
			/** =================================== */
//			SearchFactory.initDocument("I am in china boy, I am a boy. I love china");
			SearchFactory.clear();

			DBCursor cursor = word.find();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

			/** =================================== */
			System.out.println("word:" + word.count() + " doc:"
					+ doc.count() + " mapper:" + mapper.count());
			System.out.println("end time: "
					+ (System.currentTimeMillis() - start) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
