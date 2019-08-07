package com.ecity.java.mvc.service.visa.ota.system;

import java.sql.SQLException;
import java.util.Date;

import com.ecity.java.mvc.dao.uilts.SQLUilts;
import com.ecity.java.mvc.model.visa.ota.system.ProductInfoPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class ProductInfoService implements IProductInfoService {

  @Override
  public ProductInfoPO find(String id) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Epi_Product_Info where Epi_status<>'D' and Epi_id=" + id);
    try {
      table.Open();
      if (table.next()) {
        ProductInfoPO info = new ProductInfoPO();
        info.DBToBean(table, "Epi");
        return info;
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return null;
  }

  @Override
  public ProductInfoPO insert() {
    // TODO Auto-generated method stub
    ProductInfoPO info = new ProductInfoPO();
    String id = "";
    try {
      id = SQLUilts.GetMaxID(SQLCon.GetConnect(), "5");
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    info.set_id(Integer.parseInt(id));
    info.set_Code(id);
    info.set_status("I");
    info.set_Date_Ins(new Date());
    return info;
  }

}
