package com.rai.search.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.Lexeme;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MongoException;

public class SearchFactory {

	/**
	 * 初始化文本内容
	 * 
	 * @param text
	 * @throws MongoException
	 * @throws IOException 
	 */
	public static void initDocument(String text) throws MongoException, IOException {
		DB db = DBFactory.getInstance();
		DBCollection wordColl = db.getCollection("word");
		DBCollection docColl = db.getCollection("document");
		DBCollection mapperColl = db.getCollection("word_mapper");

		// text = "I am in china boy and I am a boy and I love china";

		Reader in=new StringReader(text);
		org.wltea.analyzer.IKSegmentation seg=new org.wltea.analyzer.IKSegmentation(in);
		Lexeme word=null;
		
		// insert doc
		BasicDBObject _doc = new BasicDBObject("content", text);
		docColl.insert(_doc);

		DBRef docRef = new DBRef(db, "document", _doc.get("_id"));
		DBCollection tempColl = db.getCollection("temp" + _doc.get("_id"));

		for(word=seg.next();word!=null;word=seg.next())
		{	
			String key=word.getLexemeText();
		
			// save word
			BasicDBObject query = new BasicDBObject("key", key);
			DBObject _word = wordColl.findOne(query);
			if (_word == null) {
				_word = new BasicDBObject();
				_word.put("count", 1);
				_word.put("key", key);
				wordColl.insert(_word);
			} else {
				Integer count = (Integer) _word.get("count");
				_word.put("count", count + 1);
				wordColl.save(_word);
			}

			// save temp
			BasicDBObject _temp = new BasicDBObject();
			_temp.put("document", docRef);
			_temp.put("word", new DBRef(db, "word", _word.get("_id")));
			tempColl.insert(_temp);
		}

		// count word&document data to mapper
		List<DBRef> wordRefList = tempColl.distinct("word");
		for (DBRef wordRef : wordRefList) {
			BasicDBObject query = new BasicDBObject();
			query.put("word", wordRef);

			long count = tempColl.count(query);

			// save mapper
			BasicDBObject _mapper = new BasicDBObject();
			_mapper.put("document", docRef);
			_mapper.put("word", wordRef);
			_mapper.put("count", count);

			mapperColl.insert(_mapper);
		}

		tempColl.drop();

	}

	public static List<DBObject> search(String keyword)
			throws UnknownHostException, MongoException {
		DB db = DBFactory.getInstance();
		DBCollection wordColl = db.getCollection("word");
		DBCollection docColl = db.getCollection("document");
		DBCollection mapperColl = db.getCollection("word_mapper");

		BasicDBObject query = new BasicDBObject();
		query.put("key", keyword);
		DBObject _word = wordColl.findOne(query);
		List<DBObject> list = new ArrayList<DBObject>();
		if (_word != null) {
			DBRef wordRef = new DBRef(db, "word", _word.get("_id"));
			DBCursor cursor = mapperColl.find(
					new BasicDBObject("word", wordRef)).sort(
					new BasicDBObject("count", -1));

			while (cursor.hasNext()) {
				DBObject _doc = ((DBRef) cursor.next().get("document")).fetch();
				list.add(_doc);
			}
		}
		return list;

	}

	public static void clear() throws UnknownHostException, MongoException {
		DB db = DBFactory.getInstance();
		DBCollection wordColl = db.getCollection("word");
		DBCollection docColl = db.getCollection("document");
		DBCollection mapperColl = db.getCollection("word_mapper");

		mapperColl.drop();
		docColl.drop();
		wordColl.drop();
	}

	/**
	 * 分词
	 * 
	 * @param text
	 * @return
	 */
	public static String[] split(String text) {
		return text.split(" ");
	}
}
