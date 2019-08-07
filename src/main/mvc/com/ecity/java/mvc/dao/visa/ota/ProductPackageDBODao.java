package com.ecity.java.mvc.dao.visa.ota;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.ota.ProductPackageDBO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class ProductPackageDBODao implements IProductPackageDBODao {

  @Override
  public ArrayList<ProductPackageDBO> Find(String code) {
    // TODO Auto-generated method stub
    ArrayList<ProductPackageDBO> boList = new ArrayList<ProductPackageDBO>();
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Epgd_Name,Epgd_SaleMoney,Epgd_CostMoney,Epi_id,Epi_code,Epi_Type,Epi_id_act,Epi_Day,Epi_id_Esi,Epi_id_Eva\r\n"
            + "from Epgm_Product_Package_M,Epgd_Product_Package_d ,Epi_Product_Info\r\n"
            + "where Epgm_id=Epgd_id_Epgm and Epgd_id_Epi=Epi_id\r\n"
            + "and Epgm_status<>'D' and Epgd_status<>'D' and Epi_status<>'D'\r\n" + "and Epgm_CodeOutSide='" + code
            + "'");
    try {
      try {
        table.Open();
        while (table.next()) {
          ProductPackageDBO bo = new ProductPackageDBO();
          bo.DBToBean(table);
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
