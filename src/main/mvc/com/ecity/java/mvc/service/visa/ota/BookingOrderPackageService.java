package com.ecity.java.mvc.service.visa.ota;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.ota.BookingOrderPackagePO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class BookingOrderPackageService implements IBookingOrderPackageService {

  @Override
  public ArrayList<BookingOrderPackagePO> findByeboID(String eboID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Ebop_BookingOrder_Package where ebop_id_ebo=" + eboID);
    try {
      ArrayList<BookingOrderPackagePO> packageList = new ArrayList<BookingOrderPackagePO>();
      table.Open();
      while (table.next()) {
        BookingOrderPackagePO p = new BookingOrderPackagePO();
        p.DBToBean(table, "Ebop");
        packageList.add(p);
      }
      return packageList;
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return null;
  }

  @Override
  public String Name(String ebonID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ebon_Name from Ebon_BookingOrder_NameList where ebon_id=" + ebonID);
    try {
      table.Open();
      if (table.next()) {
        try {
          return table.getString("Ebon_Name");
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
    } finally {
      table.CloseAndFree();
    }
    return "";
  }

  @Override
  public String PassPort(String ebonID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ebon_PassPort from Ebon_BookingOrder_NameList where ebon_id=" + ebonID);
    try {
      table.Open();
      if (table.next()) {
        try {
          return table.getString("Ebon_PassPort");
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
    } finally {
      table.CloseAndFree();
    }
    return "";
  }

  @Override
  public String GetAvgID(String ebonID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ebon_id_avg from Ebon_BookingOrder_NameList where ebon_id=" + ebonID);
    try {
      table.Open();
      if (table.next()) {
        try {
          return table.getString("Ebon_id_avg");
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
    } finally {
      table.CloseAndFree();
    }
    return "";
  }

}
