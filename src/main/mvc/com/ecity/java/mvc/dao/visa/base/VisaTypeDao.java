package com.ecity.java.mvc.dao.visa.base;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.base.VisaTypePO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class VisaTypeDao implements IVisaTypeDao {

  @Override
  public ArrayList<VisaTypePO> Find() {
    ArrayList<VisaTypePO> boList = new ArrayList<VisaTypePO>();

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Evt_Visa_Type where Evt_status<>'D' order by Evt_index");
    try {
      try {
        table.Open();

        while (table.next()) {
          VisaTypePO bo = new VisaTypePO();
          bo.DBToBean(table, "Evt");
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
