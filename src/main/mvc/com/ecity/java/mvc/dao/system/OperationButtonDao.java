package com.ecity.java.mvc.dao.system;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.mvc.model.system.OperationButtonPO;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class OperationButtonDao {

  public ArrayList<OperationButtonPO> find(String ID)
  {
    ArrayList<OperationButtonPO> List = new ArrayList<OperationButtonPO>();
    String SqlStr="";
    if (ID.equals(""))
    {
      SqlStr="select * from sob_Operation_button where sob_status<>'D' order by sob_index";
    }
    else
    {
      SqlStr="select * from sob_Operation_button where sob_id="+ID+" and sob_status<>'D' order by sob_index";
    }
    DBTable table=new DBTable(SQLCon.GetConnect(),SqlStr);
    try {
      table.Open();
      while (table.next()) {
        OperationButtonPO data = new OperationButtonPO();
        data.DBToBean(table, "sob");
        List.add(data);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
      table.Close();
    }
    return List;    
  }
}
