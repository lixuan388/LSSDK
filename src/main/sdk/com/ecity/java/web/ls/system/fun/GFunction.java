package com.ecity.java.web.ls.system.fun;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.bson.Document;

import com.ecity.java.json.JSONObject;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

public class GFunction {

  public static String Bass64Encode(String text) {
    Base64 base64 = new Base64();
    byte[] textByte;
    try {
      textByte = text.getBytes("UTF-8");
      return base64.encodeToString(textByte);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    }

  }

  public static String Bass64Decode(String text) {
    Base64 base64 = new Base64();

    try {
      return new String(base64.decode(text), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    }

  }

  public static String RandomUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().replaceAll("-", "").toUpperCase();
  }

  public static void SendMessageBox(int UserSend, int SourceType, int SourceID, int SendType) {
    CallableStatement cstmtGetID = null;
    try {
      cstmtGetID = SQLCon.GetConnect().prepareCall("{call dbo.PMB_SendMessageBox(?, ?, ?, ?)}");
      cstmtGetID.setInt(1, UserSend);
      cstmtGetID.setInt(2, SourceType);
      cstmtGetID.setInt(3, SourceID);
      cstmtGetID.setInt(4, SendType);
      cstmtGetID.execute();

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static boolean isWechat(HttpServletRequest request) {
    String ua = request.getHeader("User-Agent").toLowerCase();
    System.out.println(ua + "////" + ua.indexOf("micromessenger"));
    ;
    if (ua.indexOf("micromessenger") > -1) {
      return true;// 微信
    }
    return false;// 非微信手机浏览器
  }
  public static void TimeTaskLog(String Name,String remark,String timeRemark) {

      MongoDatabase database = MongoCon.GetConnect();

      MongoCollection<Document> collection = database.getCollection("TimeTaskLog");

      JSONObject logJson=new JSONObject();
      
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date=new Date();
      logJson.put("TimeTaskName", Name);
      logJson.put("remark", remark);
      logJson.put("timeRemark", timeRemark);
      logJson.put("DateString", fmt.format(date));
      logJson.put("Date",date.getTime());
      
      Document document = Document.parse(logJson.toString());

    try {      
      collection.updateMany(Filters.eq("TimeTaskName", Name),
              new Document("$set", document), new UpdateOptions().upsert(true));
    } catch (MongoException e) {
      // TODO: handle exception
      e.printStackTrace();
    }

  }

}
