package com.ecity.java.mvc.dao.visa.base;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.base.OrderStatusPO;
import com.ecity.java.mvc.model.visa.base.VisaTypePO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class OrderStatusDao implements IOrderStatusDao {

  @Override
  public ArrayList<OrderStatusPO> Find2() {
    ArrayList<OrderStatusPO> boList = new ArrayList<OrderStatusPO>();

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_type=2 order by eos_index");
    try {
      try {
        table.Open();

        while (table.next()) {
          OrderStatusPO bo = new OrderStatusPO();
          bo.DBToBean(table, "eos");
          boList.add(bo);
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return boList;
    } finally {
      table.Close();
    }

  }

  @Override
  public ArrayList<OrderStatusPO> Find1() {
    // TODO Auto-generated method stub
    ArrayList<OrderStatusPO> boList = new ArrayList<OrderStatusPO>();

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from eos_OrderStatus where eos_status<>'D' and eos_type=1 order by eos_index");
    try {
      try {
        table.Open();

        while (table.next()) {
          OrderStatusPO bo = new OrderStatusPO();
          bo.DBToBean(table, "eos");
          boList.add(bo);
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return boList;
    } finally {
      table.Close();
    }

  }

}
