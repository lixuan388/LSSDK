package com.ecity.java.mvc.dao.visa.base;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.base.VisaAreaPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class VisaAreaDao implements IVisaAreaDao {

  @Override
  public ArrayList<VisaAreaPO> Find() {
    // TODO Auto-generated method stub

    ArrayList<VisaAreaPO> boList = new ArrayList<VisaAreaPO>();

    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Eva_Visa_Area where Eva_status<>'D' order by Eva_index");
    try {
      try {
        table.Open();
        while (table.next()) {
          VisaAreaPO bo = new VisaAreaPO();
          bo.DBToBean(table, "Eva");
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
  public VisaAreaPO FindByID(String ID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Eva_Visa_Area where Eva_status<>'D' and Eva_id=" + ID + " order by Eva_index");
    try {
      try {
        table.Open();

        if (table.next()) {
          VisaAreaPO bo = new VisaAreaPO();
          bo.DBToBean(table, "Eva");
          return bo;
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
