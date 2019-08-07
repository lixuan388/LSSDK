package com.ecity.java.mvc.dao.uilts;

import java.util.Date;


import org.bson.Document;

import com.ecity.java.json.JSONObject;
import com.ecity.java.web.WebFunction;
import com.java.sql.MongoCon;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class ErrorLog {

  public static final int BeanPostData=1;
  public static final int TablePostData=2;

  public static void log(int ErrorType,String ErrorMsg,int ErrorCode,JSONObject Data)
  {
    try
    {
      MongoDatabase database = MongoCon.GetConnect();
      MongoCollection<Document> collection = database.getCollection("ErrorLog");
  
      Document documents=new Document();  
      documents.put("ErrorType", ErrorType);
      documents.put("ErrorMsg", ErrorMsg);
      documents.put("ErrorCode", ErrorCode);
      DBObject bson = (DBObject)JSON.parse(Data.toString());
      documents.put("Data", bson);
      Date d=new Date();
      String now=WebFunction.FormatDate(WebFunction.Format_YYYYMMDDHHMMSS, d);
      documents.put("InsDate", now);
//      System.out.println(documents.toJson());
      collection.insertOne(documents);
    }
    catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }
}
