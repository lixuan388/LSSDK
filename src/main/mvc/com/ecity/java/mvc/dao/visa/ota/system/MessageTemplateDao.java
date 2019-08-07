package com.ecity.java.mvc.dao.visa.ota.system;

import java.sql.SQLException;
import java.util.ArrayList;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.uilts.TablePostData;
import com.ecity.java.mvc.model.visa.ota.BookingOrderPackagePO;
import com.ecity.java.mvc.model.visa.ota.system.MessageTemplatePO;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.sql.SQLCon;

public class MessageTemplateDao implements IMessageTemplateDao {

  @Override
  public ArrayList<MessageTemplatePO> Find() {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Emt_MessageTemplate where Emt_status<>'D' order by Emt_id");
    try {
      ArrayList<MessageTemplatePO> list = new ArrayList<MessageTemplatePO>();
      try {
        table.Open();
        while (table.next()) {
          MessageTemplatePO p = new MessageTemplatePO();
          p.DBToBean(table, "Emt");
          list.add(p);
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return list;
    } finally {
      table.CloseAndFree();
    }

  }

  @Override
  public MessageTemplatePO FindByID(String ID) {
    // TODO Auto-generated method stub
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select * from Emt_MessageTemplate where Emt_status<>'D' and Emt_id=" + ID);
    try {
      try {
        table.Open();

        if (table.next()) {
          MessageTemplatePO p = new MessageTemplatePO();
          p.DBToBean(table, "Emt");
          return p;
        } else {
          return null;
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
    } finally {
      table.CloseAndFree();
    }
  }

  @Override
  public JSONObject Post(JSONArray data) {
    // TODO Auto-generated method stub
    TablePostData post = new TablePostData("Emt_MessageTemplate", "Emt_id", data);
    return post.Post();
  }

  @Override
  public JSONObject DeleteDelete(String ID, String UserName) {
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    try {
      boolean result = update.Execute("update Emt_MessageTemplate set Emt_Status='D',Emt_User_Lst='" + UserName
          + "',Emt_Date_Lst=GETDATE() where Emt_id=" + ID);
      if (!result) {
        return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
      } else {
        return WebFunction.WriteMsgToJson(1, "Success");
      }
    } finally {
      update.CloseAndFree();
    }
  }

}
