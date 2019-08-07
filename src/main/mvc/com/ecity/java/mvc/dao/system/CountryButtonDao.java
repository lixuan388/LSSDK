package com.ecity.java.mvc.dao.system;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.model.system.OperationButtonPO;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class CountryButtonDao {

  public JSONObject Post(JSONArray DataRows) 
  {
    
    JSONObject ReturnJson = new JSONObject();

    ReturnJson.put("MsgID","1");
    ReturnJson.put("MsgText","Success");
    //System.out.println(DataRows.toString());
    for (int i=0;i<DataRows.length();i++)
    {
      JSONObject data=DataRows.getJSONObject(i);

      DBTable table=new DBTable(SQLCon.GetConnect());
      try
      {

        String acb_id_act=data.getString("acb_id_act");
        String acb_id_sob=data.getString("acb_id_sob");
        String acb_flag=data.getString("acb_flag");
        
        //System.out.println("KeyValue:"+KeyValue);
        table.SQL("select * from acb_country_button where acb_id_act="+acb_id_act+" and acb_id_sob="+acb_id_sob+" and acb_status<>'D'");
        table.Open();
        if (!table.next())
        {
          table.insertRow();
          table.UpdateValue("acb_status", "I");
          table.UpdateValue("acb_date_ins", new Date(new java.util.Date().getTime()));
          table.UpdateValue("acb_id_act", acb_id_act);
          table.UpdateValue("acb_id_sob", acb_id_sob);
        }    
        else
        {
          table.UpdateValue("acb_status", "E");
          table.UpdateValue("acb_date_lst", new Date(new java.util.Date().getTime()));
        }
        table.UpdateValue("acb_flag", acb_flag);
        table.PostRow();
      }catch (SQLException e) {
        // TODO Auto-generated catch block
        ReturnJson.put("MsgID","-1");
        ReturnJson.put("MsgText",e.getMessage());
        e.printStackTrace();
        return ReturnJson;
      }
      finally
      {
        table.CloseAndFree();
      }  
    }
    return ReturnJson;
  }  
  
  public JSONObject PostByText(JSONArray DataRows) 
  {
    
    JSONObject ReturnJson = new JSONObject();

    ReturnJson.put("MsgID","1");
    ReturnJson.put("MsgText","Success");
    //System.out.println(DataRows.toString());
    StringBuilder UpdateSql = new StringBuilder();
    System.out.println("begin PostByText");
    DBTable table=new DBTable(SQLCon.GetConnect());      
    try
    {
      for (int i=0;i<DataRows.length();i++)
      {
        JSONObject data=DataRows.getJSONObject(i);
  
  
  
          String acb_id_act=data.getString("acb_id_act");
          String acb_id_sob=data.getString("acb_id_sob");
          String acb_flag=data.getString("acb_flag");
          
          //System.out.println("KeyValue:"+KeyValue);
          table.SQL("select * from acb_country_button where acb_id_act="+acb_id_act+" and acb_id_sob="+acb_id_sob+" and acb_status<>'D'");
          table.Open();
          if (!table.next())
          {
            UpdateSql.append("insert into acb_country_button (acb_status,acb_date_ins,acb_id_act,acb_id_sob,acb_flag)");
            UpdateSql.append("values ('I',getdate(),"+acb_id_act+","+acb_id_sob+","+acb_flag+")");
          }
          else
          {
            UpdateSql.append("update acb_country_button  set acb_status='E',acb_date_lst=getdate(),acb_flag="+acb_flag+" where acb_id="+table.getString("acb_id"));
          }
  
      }      
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      ReturnJson.put("MsgID","-1");
      ReturnJson.put("MsgText",e.getMessage());
      e.printStackTrace();
      return ReturnJson;
    }
    finally
    {
      table.CloseAndFree();
    }  
    String sql=UpdateSql.toString();
    if (!sql.equals(""))
    {
      DBQuery update=new DBQuery(SQLCon.GetConnect());
      System.out.println("do update sql");
      if (!update.Execute(sql))
      {
        ReturnJson.put("MsgID","-1");
        ReturnJson.put("MsgText","数据保存失败！请重试！");
      }
      update.CloseAndFree();
    }
    return ReturnJson;
  }
}
