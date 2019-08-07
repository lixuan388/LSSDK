package com.ecity.java.mvc.dao.SF;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import com.ecity.java.mvc.model.SF.WayBillOrderPO;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class WayBillOrderDao {


  public boolean insert(String _user_ins, String _SourceOrderNo, String _OrderID, String _Type, String _Content,String _MailNo) {
    // TODO Auto-generated method stub
    DBQuery query = new DBQuery(SQLCon.GetConnect());

    Base64.Encoder encoder = Base64.getEncoder();
    byte[] textByte = null;
    try {
      textByte = _Content.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String Content = encoder.encodeToString(textByte);
    boolean result=query.Execute(
        "insert into ewbo_WayBillOrder(ewbo_user_ins,ewbo_date_ins,ewbo_OrderID,ewbo_SourceOrderNo,ewbo_Type,ewbo_Content,ewbo_MailNo)\r\n"
            + "values ('" + _user_ins + "',GETDATE(),'" + _OrderID + "','" + _SourceOrderNo + "','" + _Type + "','"
            + Content + "','"+_MailNo+"')");
    query.CloseAndFree();
    return result;
  }
  public void insert(String _user_ins, String[] _SourceOrderNoList, String _OrderID, String _Type, String _Content,String _MailNo) {
    // TODO Auto-generated method stub
    for (int i=0;i<_SourceOrderNoList.length;i++) {
      insert(_user_ins, _SourceOrderNoList[i], _OrderID, _Type, _Content,_MailNo); 
    }
    
  }

  public ArrayList<WayBillOrderPO> FindByCode(String Code) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from ewbo_WayBillOrder where ewbo_SourceOrderNo='" + Code + "'");
    ArrayList<WayBillOrderPO> orderList = new ArrayList<WayBillOrderPO>();
    try {
      try {
        table.Open();
        while (table.next()) {
          WayBillOrderPO order = new WayBillOrderPO();
          order.DBToBean(table, "ewbo");
          orderList.add(order);
        }

      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } finally {
      table.Close();
    }
    return orderList;
  }

}
