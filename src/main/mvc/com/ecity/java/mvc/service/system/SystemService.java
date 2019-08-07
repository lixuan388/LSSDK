package com.ecity.java.mvc.service.system;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class SystemService implements ISystemService {

  @Override
  public String GetProductTypeName(String id) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ept_Name from dbo.Ept_Product_Type where Ept_status<>'D' and Ept_id=" + id);
    try {
      try {
        table.Open();
        if (table.next()) {
          try {
            return table.getString("Ept_Name");
          } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
          }
        } else {
          return "";
        }
      } catch (SQLException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    } finally {
      table.CloseAndFree();
    }
    return "";
  }

  @Override
  public String GetCountryName(String id) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select act_name from act_country where act_status<>'D' and act_id=" + id);
    try {
      table.Open();
      if (table.next()) {
        try {
          return table.getString("act_name");
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          return "";
        }
      } else {
        return "";
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    } finally {
      table.CloseAndFree();
    }
  }

  @Override
  public String GetSupplierName(String id) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select esi_Name from esi_Supplier_Info where esi_status<>'D' and esi_id=" + id);
    try {
      table.Open();
      if (table.next()) {
        try {
          return table.getString("esi_Name");
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          return "";
        }
      } else {
        return "";
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    } finally {
      table.CloseAndFree();
    }
  }

  @Override
  public String[] GetBookingOrderStatusName() {
    // TODO Auto-generated method stub
    String[] StatusName = { "未完成", "已完成", "已发货", "未认领", "已认领", "更新申请人", "无办理人信息", "办理人已填写", "已收到资料", "已审核完成", "已送签",
        "结果已返回", "已预约面试", "处理中", "买家已填写反馈信息", "已中止办理", "需补充资料","已寄回结果", "已退回资料", "结果返回" };
    return StatusName;
  }
  @Override
  public JSONArray GetBookingOrderStatusNameJson() {
    // TODO Auto-generated method stub
    JSONArray returnJson=new JSONArray();
    String[] StatusName = GetBookingOrderStatusName();
    for (int i=0;i<StatusName.length;i++)
    {
      returnJson.put(StatusName[i]);
    }
    return returnJson;
  }
  

  public JSONObject GetOrderStatusJson() {
    // TODO Auto-generated method stub
    JSONObject returnJson=new JSONObject();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select eos_code,eos_name from dbo.eos_OrderStatus where eos_type=2 order by eos_code");
    try {
      table.Open();
      while  (table.next()) {
        returnJson.put(table.getString("eos_code"), table.getString("eos_name"));
      } 
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return returnJson;
  }

  
  

  @Override
  public String GetVisaReturnDate(String avsID, String actID, String SendDate) {
    // TODO Auto-generated method stub
    String value = "";
    try {
      Connection conn = SQLCon.GetConnect();
      CallableStatement c = conn.prepareCall("{call pv_GetReturnDate(?,?,?,?)}");// 调用带参的存储过程
      // 给存储过程的参数设置值
      c.setString(1, avsID); // 将第一个参数的值设置成测试
      c.setString(2, actID); // 将第一个参数的值设置成测试
      c.setString(3, SendDate); // 将第一个参数的值设置成测试
      c.registerOutParameter(4, java.sql.Types.VARCHAR);// 第二个是返回参数 返回未Integer类型
      // 执行存储过程
      c.execute();
      value = c.getString(4);
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return value;
  }
  @Override
  public String GetVisaNextWorkDate(String avsID, String actID, String SendDate) {
    // TODO Auto-generated method stub
    String value = "";
    try {
      Connection conn = SQLCon.GetConnect();
      CallableStatement c = conn.prepareCall("{call pv_GetNextWrokDate(?,?,?,?)}");// 调用带参的存储过程
      // 给存储过程的参数设置值
      c.setString(1, avsID); // 将第一个参数的值设置成测试
      c.setString(2, actID); // 将第一个参数的值设置成测试
      c.setString(3, SendDate); // 将第一个参数的值设置成测试
      c.registerOutParameter(4, java.sql.Types.VARCHAR);// 第二个是返回参数 返回未Integer类型
      // 执行存储过程
      c.execute();
      value = c.getString(4);
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return value;
  }

}
