package com.ecity.java.mvc.dao.visa.ota;

import java.sql.SQLException;

import com.ecity.java.mvc.model.visa.ota.ProductPackageMPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class ProductPackageMDao implements IProductPackageMDao {

  @Override
  public ProductPackageMPO Find(String code) {
    // TODO Auto-generated method stub
    ProductPackageMPO po = new ProductPackageMPO();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Epgm_Product_Package_M where Epgm_CodeOutSide='" + code + "'");
    try {
      try {
        table.Open();
        if (table.next()) {
          po.DBToBean(table, "Epgm");
          return po;
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
