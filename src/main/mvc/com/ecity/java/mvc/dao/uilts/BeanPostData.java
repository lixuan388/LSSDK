package com.ecity.java.mvc.dao.uilts;

import java.sql.SQLException;
import java.util.Iterator;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.sql.SQLCon;

public class BeanPostData {

  String TableName;
  String TableKey;
  JSONArray DataRows;
  String AutoGenID;
  String TablePrefix;

  public BeanPostData(String TableName, String TableKey, String TablePrefix, JSONArray DataRows) {
    this(TableName, TableKey, TablePrefix, DataRows, "5");
  }

  public BeanPostData(String TableName, String TableKey, String TablePrefix, JSONArray DataRows, String AutoGenID) {
    this.TableName = TableName;
    this.TableKey = TableKey;
    this.DataRows = DataRows;
    this.AutoGenID = AutoGenID;
    this.TablePrefix = TablePrefix;
  }

  public JSONObject Post() {
    JSONObject ReturnJson = new JSONObject();

    ReturnJson.put("MsgID", "1");
    ReturnJson.put("MsgText", "Success");
    String KeyValue = "";
    // System.out.println(DataRows.toString());
    for (int i = 0; i < DataRows.length(); i++) {
      JSONObject data = DataRows.getJSONObject(i);

      DBTable table = new DBTable(SQLCon.GetConnect());
      try {
        KeyValue = data.getString(TableKey);
        // System.out.println("KeyValue:"+KeyValue);

        table.SQL("select * from " + TableName + " where " + this.TablePrefix + TableKey + "='" + KeyValue + "'");
        table.Open();
        if (!table.next()) {
          // System.out.println("table.moveToInsertRow()");
          table.insertRow();
          if (KeyValue.equals("-1")) {
            try {
              KeyValue = SQLUilts.GetMaxID(SQLCon.GetConnect(), this.AutoGenID);
            } catch (Exception e) {
              e.printStackTrace();
              return WebFunction.WriteMsgToJson(-1, "获取ID失败！");
            }
          }
        }
        Iterator<String> iter = data.keys();
        while (iter.hasNext()) {
          String JsonKeyName = iter.next();
          String FieldName = this.TablePrefix + JsonKeyName;
          String value = data.getString(JsonKeyName);
          if (!TableKey.equals(JsonKeyName)) {
//						System.out.println(JsonKeyName+":"+value);
//						System.out.println(FieldName+":"+table.getColumnType(FieldName));
            if ((table.getColumnType(FieldName) == 93) && (value.equals(""))) {
              table.UpdateValue(FieldName, null);
            } else {
              table.UpdateValue(FieldName, value);
            }
          } else {
            table.UpdateValue(this.TablePrefix + TableKey, KeyValue);
          }
//						System.out.println(str);
        }
        if (!table.PostRow()) {
          return WebFunction.WriteMsgToJson(-1, "数据保存失败！请重试！");
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
//				ReturnJson.put("MsgID","-1");
//				ReturnJson.put("MsgText",e.getMessage());
        e.printStackTrace();
        ErrorLog.log(ErrorLog.BeanPostData, e.getMessage(), 1, data);
        return WebFunction.WriteMsgToJson(-1, "数据保存失败！");
      } finally {
        table.CloseAndFree();
      }
    }
    ReturnJson.put("Key", KeyValue);
    return ReturnJson;
  }
}
