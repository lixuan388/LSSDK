package com.ecity.java.mvc.dao.visa.ota;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.uilts.TablePostData;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

/**
 * dao类
 */
public class BookingOrderNameListImpl implements IBookingOrderNameList {

  @Override
  public BookingOrderNameListPO find(String id) {
    // TODO Auto-generated method stub
    BookingOrderNameListPO model = new BookingOrderNameListPO();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Ebon_BookingOrder_NameList where ebon_id=" + id + " order by ebon_id");
    try {
      try {
        table.Open();

        if (table.next()) {
          model.DBToBean(table, "Ebon");
          return model;
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } finally {
      table.Close();
    }
    return null;
  }

  @Override
  public ArrayList<BookingOrderNameListPO> findByOrderNo(String OrderNo) {
    // TODO Auto-generated method stub
    ArrayList<BookingOrderNameListPO> modelList = new ArrayList<BookingOrderNameListPO>();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ebon_BookingOrder_NameList.* from Ebon_BookingOrder_NameList,dbo.Ebo_BookingOrder where Ebo_id=Ebon_id_Ebo\r\n"
            + "and ebo_sourceorderno='" + OrderNo + "'  order by ebon_id");
    try {
      try {
        table.Open();

        while (table.next()) {
          BookingOrderNameListPO model = new BookingOrderNameListPO();
          model.DBToBean(table, "Ebon");
          modelList.add(model);
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } finally {
      table.Close();
    }
    return modelList;
  }

  @Override
  public ArrayList<BookingOrderNameListPO> findByEboID(String id) {
    // TODO Auto-generated method stub
    ArrayList<BookingOrderNameListPO> modelList = new ArrayList<BookingOrderNameListPO>();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Ebon_BookingOrder_NameList where ebon_id_ebo=" + id);
    try {
      try {
        table.Open();
        while (table.next()) {
          BookingOrderNameListPO model = new BookingOrderNameListPO();
          model.DBToBean(table, "Ebon");
          modelList.add(model);
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } finally {
      table.Close();
    }
    return modelList;
  }

  @Override
  public JSONObject PostData(JSONObject data) {
    // TODO Auto-generated method stub
    JSONArray DataRows = data.getJSONArray("DataRows");
    TablePostData table = new TablePostData("Ebon_BookingOrder_NameList", "ebon_id", DataRows);
    JSONObject ReturnJson = table.Post();
    return ReturnJson;
  }
}
