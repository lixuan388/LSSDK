package com.ecity.java.mvc.dao.uilts;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;

import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class SQLUilts {
  public static String GetMaxID(Connection conn, String AutoGenID) throws SQLException {
    CallableStatement c = conn.prepareCall("{call uspGetMaxId(?,?)}");// 调用带参的存储过程
    // 给存储过程的参数设置值
    c.setString(1, AutoGenID); // 将第一个参数的值设置成测试
    c.registerOutParameter(2, java.sql.Types.VARCHAR);// 第二个是返回参数 返回未Integer类型
    // 执行存储过程
    c.execute();
    String KeyValue = c.getString(2);
    conn.close();
    return KeyValue;
  }

  public static String GetMaxID() throws SQLException {
    return GetMaxID(SQLCon.GetConnect(), "5");
  }

  public static String GetMaxIDByDatePrefix(String prefix) throws SQLException {
    String KeyID;
    KeyID = GetMaxID();

    java.util.Date ss = new java.util.Date();
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
    String now = format.format(ss);// 这个就是把时间戳经过处理得到期望格式的时间
    return prefix + now + KeyID;
  }

  public static void UpdateInvoiceStatusType() {
    try {
      Connection conn = SQLCon.GetConnect();
      CallableStatement c = conn.prepareCall("{call pi_UpdateInvoiceStatusType()}");// 调用带参的存储过程
      // 给存储过程的参数设置值
      // 执行存储过程
      c.execute();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String  GetInvoicenoOutput(int actID) {
    String value = "";
    try {
      Connection conn = SQLCon.GetConnect();
      CallableStatement c = conn.prepareCall("{call usp_get_invoiceno_output(?,?)}");// 调用带参的存储过程
      // 给存储过程的参数设置值
      c.setInt(1, actID); // 将第一个参数的值设置成测试
      c.registerOutParameter(2, java.sql.Types.VARCHAR);// 第二个是返回参数 返回未Integer类型
      // 执行存储过程
      c.execute();
      value = c.getString(2);
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return value;
  }

  public static JSONObject DBToJson(DBTable table, String prefix) throws SQLException {
    int ColumnCount = table.getColumnCount();
    JSONObject DataJson = new JSONObject();
    for (int i = 1; i <= ColumnCount; i++) {
      String FieldName = table.getColumnLabel(i);

      FieldName = toLowerCaseFirstOne(FieldName.replaceAll(prefix, ""));

      Object FieldValue = table.getString(FieldName);
      DataJson.put(FieldName, FieldValue);
    }
    return DataJson;
  }

  public static String toLowerCaseFirstOne(String s) {
    if (Character.isLowerCase(s.charAt(0)))
      return s;
    else
      return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
  }

}
