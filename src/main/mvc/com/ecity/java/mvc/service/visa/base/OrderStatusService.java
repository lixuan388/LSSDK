package com.ecity.java.mvc.service.visa.base;

import java.sql.SQLException;

import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.sql.SQLCon;

public class OrderStatusService implements IOrderStatusService {

  @Override
  public JSONObject UpdateMainName(String id, String Value) {
    // TODO Auto-generated method stub

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_id=" + id);
    try {
      try {
        table.Open();
        if (table.next()) {
          table.UpdateValue("eos_MainName", Value);
          table.PostRow();
          return WebFunction.WriteMsgToJson(1, "Success！");
        } else {
          return WebFunction.WriteMsgToJson(-1, "无对应记录，无法修改！");
        }

      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return WebFunction.WriteMsgToJson(-1, "数据操作失败！<br>" + e.getMessage());
      }
    } finally {
      table.Close();
    }
  }

  @Override
  public JSONObject UpdateERPName(String id, String Value) {
    // TODO Auto-generated method stub

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_id=" + id);
    try {
      try {
        table.Open();
        if (table.next()) {
          table.UpdateValue("eos_ERPName", Value);
          table.PostRow();
          return WebFunction.WriteMsgToJson(1, "Success！");
        } else {
          return WebFunction.WriteMsgToJson(-1, "无对应记录，无法修改！");
        }

      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return WebFunction.WriteMsgToJson(-1, "数据操作失败！<br>" + e.getMessage());
      }
    } finally {
      table.Close();
    }
  }

  @Override
  public JSONObject UpdateState(String id, String Value) {
    // TODO Auto-generated method stub

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_id=" + id);
    try {
      try {
        table.Open();
        if (table.next()) {
          table.UpdateValue("eos_state", Value);
          table.PostRow();
          return WebFunction.WriteMsgToJson(1, "Success！");
        } else {
          return WebFunction.WriteMsgToJson(-1, "无对应记录，无法修改！");
        }

      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return WebFunction.WriteMsgToJson(-1, "数据操作失败！<br>" + e.getMessage());
      }
    } finally {
      table.Close();
    }
  }

  @Override
  public String GetState(String code) {
    // TODO Auto-generated method stub
    String state = "";
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_code='" + code + "'");
    try {
      try {
        table.Open();
        if (table.next()) {
          state = table.getString("eos_state");
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } finally {
      table.Close();
    }
    return state;
  }

}
