package com.ecity.java.mvc.dao.visa.ota;

import java.sql.SQLException;

import com.ecity.java.mvc.model.visa.ota.BookingOrderPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class BookingOrderImpl implements IBookingOrder {

  @Override
  public BookingOrderPO find(String id) {
    // TODO Auto-generated method stub
    BookingOrderPO model = new BookingOrderPO();
    DBTable table = new DBTable(SQLCon.GetConnect(), "select * from Ebo_BookingOrder where ebo_id=" + id);
    try {
      try {
        table.Open();

        if (table.next()) {
          model.DBToBean(table, "Ebo");
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
  public BookingOrderPO findByCode(String code) {
    // TODO Auto-generated method stub
    BookingOrderPO model = new BookingOrderPO();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Ebo_BookingOrder where Ebo_SourceOrderNo='" + code + "'");
    try {
      try {
        table.Open();

        if (table.next()) {
          model.DBToBean(table, "Ebo");
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

}
