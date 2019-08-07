package com.ecity.java.mvc.service.visa.ota;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.uilts.SQLUilts;
import com.ecity.java.mvc.dao.visa.ota.BookingOrderImpl;
import com.ecity.java.mvc.dao.visa.ota.BookingOrderNameListImpl;
import com.ecity.java.mvc.model.visa.ota.BookingOrderHistoryPO;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;
import com.ecity.java.mvc.model.visa.ota.BookingOrderPO;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class BookingOrderService  {

  public JSONObject UpdateSaleName(String eboID, String UserName) {
    // TODO Auto-generated method stub

    DBQuery update = new DBQuery(SQLCon.GetConnect());
    try {
      boolean result = update.Execute("update Ebo_BookingOrder set Ebo_SaleName='" + UserName + "',Ebo_User_Lst='"
          + UserName + "',Ebo_Date_Lst=GETDATE() where Ebo_id='" + eboID + "'");
      UpdateStatus(eboID, UserName, "已认领", " and ebo_statustype='未认领'");
      if (!result) {
        return WebFunction.WriteMsgToJson(-1, "绑定销售失败！");
      } else {
        return this.InsertHistory(eboID, UserName, "绑定销售", "绑定销售，新销售：" + UserName);
      }
    } finally {
      update.CloseAndFree();
    }

  }

  public JSONObject UpdateStatus(String eboID, String UserName, String Status, String whereStr) {

    System.out.println("UpdateStatus");
    String MainStatus = GetMainOrderStatus(Status);
    System.out.println("Status:"+Status+"/MainStatus:"+MainStatus);
    if (!MainStatus.equals("")) {
      Status = MainStatus;
    }
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    try {
      boolean result = update.Execute("update Ebo_BookingOrder set ebo_statustype='" + Status + "',Ebo_User_Lst='"
          + UserName + "',Ebo_Date_Lst=GETDATE() where Ebo_id='" + eboID + "' " + whereStr);
      if (!result) {
        return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
      } else {
        this.InsertHistory(eboID, UserName, "更改状态", "更改订单状态：" + Status);
        return WebFunction.WriteMsgToJson(1, "Success");
      }
    } finally {
      update.CloseAndFree();
    }
  }

  public int GetOrderIDByCode(String SourceOrderNo) {
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select Ebo_id from Ebo_BookingOrder where Ebo_SourceOrderNo='" + SourceOrderNo + "'");
    try {
      table.Open();
      if (table.next()) {
        return table.getInt("Ebo_id");
      } else {
        return -1;
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return -1;

  }

  public JSONObject UpdateStatusByCode(String SourceOrderNo, String UserName, String Status, String whereStr) {
    int eboID = GetOrderIDByCode(SourceOrderNo);
    return UpdateStatus("" + eboID, UserName, Status, whereStr);
  }

  public JSONObject InsertHistory(String eboID, String UserName, String Type, String Remark) {
    return InsertHistory(eboID, UserName, Type, Remark,"0");
  }

  public JSONObject InsertHistory(String eboID, String UserName, String Type, String Remark,String Money ) {
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    try {
      boolean result = update.Execute(
          "insert into Eboh_BookingOrderHistory (Eboh_status,Eboh_Date_Ins,Eboh_User_Ins,Eboh_id_ebo,Eboh_type,Eboh_Date_op,Eboh_User_op,Eboh_remark,Eboh_Money)"
              + " values ('I',getdate(),'" + UserName + "'," + eboID + ",'" + Type + "',getdate(),'" + UserName + "','"
                  + Remark + "','" + Money + "')");
      if (!result) {
        return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
      } else {
        return WebFunction.WriteMsgToJson(1, "Success");
      }
    } finally {
      update.CloseAndFree();
    }
  }

  public JSONArray BindOrderQuery(String OrderNo) {
    DBTable table=new DBTable(SQLCon.GetConnect(), "select ebo_sourcename,Ebo_SourceOrderNo,ebo_statustype,ebo_paydate,ebo_sourceguest,\r\n" + 
        "ebo_packagename,ebo_id_eva,ebo_paymoney,ebo_linkman,ebo_phone,eod_id,ebo_id,\r\n"
        + "(select count(0) from Ebon_BookingOrder_NameList where ebon_id_ebo=ebo_id) as NameCount\r\n" + 
        "from eod_orderbind,ebo_bookingorder where eod_child=Ebo_SourceOrderNo\r\n" + 
        "and eod_id in "
        + "("
          + "select eod_id from eod_orderbind where eod_status<>'D' and eod_child <>'"+OrderNo+"' and eod_parent in "
            + "("
              + "select eod_parent from eod_orderbind where eod_status<>'D' and  eod_child ='"+OrderNo+"'"
            + ")"
         + ")");
    try {
       table.Open();
       return table.toJson();
    }catch (SQLException e) {
      e.printStackTrace();
        // TODO: handle exception
    } finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
    return null;
   
  }
  

  public JSONArray GetOrderBindInfo(String OrderNo) {
    
    JSONArray DataArray=new JSONArray();
    
    DBTable table=new DBTable(SQLCon.GetConnect(), "select eod_Child from eod_orderbind where eod_status<>'D' and eod_child<>'"+OrderNo+"' and eod_parent in (select eod_parent from  eod_orderbind where eod_child='"+OrderNo+"' and eod_status<>'D')");

    BookingOrderImpl bookingOrderImpl = new BookingOrderImpl();
    BookingOrderPO bookingOrderPO = new BookingOrderPO();
    
    try {
       table.Open();
       while (table.next()) {
         String ID=table.getString("eod_Child");
         // 读取订单主表信息
         bookingOrderPO = bookingOrderImpl.findByCode(ID);
         if (bookingOrderPO == null) {

         }
         else
         {

           JSONObject OrderData=new JSONObject();
           // 读取名单列表
           BookingOrderNameListImpl nameListImpl = new BookingOrderNameListImpl();
           ArrayList<BookingOrderNameListPO> nameListPO = new ArrayList<BookingOrderNameListPO>();
           nameListPO = nameListImpl.findByOrderNo(ID);
           JSONArray NameListJson = new JSONArray();
           for (int i = 0; i < nameListPO.size(); i++) {
             JSONObject nameJson = nameListPO.get(i).toJson();
             NameListJson.put(nameJson);
           }
           OrderData.put("NameList", NameListJson);

           
           JSONObject OrderJson = bookingOrderPO.toJson();
           
           OrderJson.put("SendGoodsList",GetTypeHistory(ID, "须寄回材料备注"));
           OrderJson.put("NameCount", nameListPO.size());
           OrderData.put("OrderInfo", OrderJson);
           OrderData.put("contactor", GetPostAddress(ID));
           DataArray.put(OrderData);
           
         }
       }
    }catch (SQLException e) {
      e.printStackTrace();
      // TODO: handle exception
    } finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
    return DataArray;
  }
  


  public JSONObject BindOrder(String UserName,String Parent, String Child) {
    
    String eod_code="";

    DBTable table1=new DBTable(SQLCon.GetConnect());
    try {
      table1.SQL("select eod_parent from eod_OrderBind where eod_status='I' and eod_Child='"+Parent+"'");
      table1.Open();
      if (table1.next()) {
        eod_code=table1.getString("eod_parent");
      }
      else {
        eod_code=SQLUilts.GetMaxID(SQLCon.GetConnect(), "eod_code");
        DBQuery update = new DBQuery(SQLCon.GetConnect());
        try {
          boolean result = update.Execute(
              "insert into eod_OrderBind (eod_status,eod_user_ins,eod_date_ins,eod_Parent,eod_Child)"
                  + " values ('I','" + UserName + "',getdate(),'"+eod_code+"','"+Parent+"')");
          if (!result) {
            return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
          }
        } finally {
          update.CloseAndFree();
        }
      }
    }catch (SQLException e) {
      // TODO: handle exception
      e.printStackTrace();
      return WebFunction.WriteMsgToJson(-1,e.getMessage());
    } finally {
      // TODO: handle finally clause
      table1.CloseAndFree();
    }

    String eod_code2="";
    
    DBTable table2=new DBTable(SQLCon.GetConnect());
    try {
      table2.SQL("select eod_parent from eod_OrderBind where eod_status='I' and eod_Child='"+Child+"'");
      table2.Open();
      if (table2.next()) {
        eod_code2=table2.getString("eod_parent");
        DBQuery update = new DBQuery(SQLCon.GetConnect());
        try {
          boolean result = update.Execute("update eod_OrderBind set eod_user_lst='" + UserName + "',eod_date_lst=getdate() ,eod_Parent='"+eod_code+"' where eod_Parent='"+eod_code2+"'");
          if (!result) {
            return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
          }
        } finally {
          update.CloseAndFree();
        }
      }
      else {
        DBQuery update = new DBQuery(SQLCon.GetConnect());
        try {
          boolean result = update.Execute(
              "insert into eod_OrderBind (eod_status,eod_user_ins,eod_date_ins,eod_Parent,eod_Child)"
                  + " values ('I','" + UserName + "',getdate(),'"+eod_code+"','"+Child+"')");
          if (!result) {
            return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
          }
        } finally {
          update.CloseAndFree();
        }
      }
    }catch (SQLException e) {
      // TODO: handle exception
      e.printStackTrace();
      return WebFunction.WriteMsgToJson(-1,e.getMessage());
    } finally {
      // TODO: handle finally clause
      table2.CloseAndFree();
    }
    return WebFunction.WriteMsgToJson(1, "Success");
    
  }

  public JSONObject UnBindOrder(String UserName,String Parent,String Child) {
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    try {
      boolean result = update.Execute("update eod_OrderBind set eod_status='D',eod_user_lst='"+UserName+"',eod_date_lst=getdate() where eod_Child='"+Child+"'  and eod_status<>'D'");
      if (!result) {
        return WebFunction.WriteMsgToJson(-1, "操作记录保存失败！");
      } else {
        return WebFunction.WriteMsgToJson(1, "Success");
      }
    } finally {
      update.CloseAndFree();
    }
  }

  public ArrayList<BookingOrderHistoryPO> GetHistoryList(String eboID) {
    ArrayList<BookingOrderHistoryPO> list = new ArrayList<BookingOrderHistoryPO>();
    DBTable table = new DBTable(SQLCon.GetConnect(), "select * from Eboh_BookingOrderHistory where eboh_id_ebo="
        + eboID + " and eboh_status<>'D' order by eboh_date_op");
    try {
      table.Open();
      while (table.next()) {
        BookingOrderHistoryPO history = new BookingOrderHistoryPO();
        history.DBToBean(table, "Eboh");
//				System.out.println(history.toJson().toString());
        list.add(history);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return list;

  }

  public float getCountryVisaSpeedMoney(int avsID, int asiID) {
    // TODO Auto-generated method stub
    float value = 0f;
//    System.out.println("------getCountryVisaSpeedMoney------");
//    System.out.println("avsID:" + avsID);
//    System.out.println("asiID:" + asiID);

    try {
      Connection conn = SQLCon.GetConnect();
      CallableStatement c = conn.prepareCall("{call pv_getCountryVisaSpeedMoney(?,?,?)}");// 调用带参的存储过程
      // 给存储过程的参数设置值
      c.setInt(1, avsID); // 将第一个参数的值设置成测试
      c.setInt(2, asiID); // 将第一个参数的值设置成测试
      c.registerOutParameter(3, java.sql.Types.FLOAT);// 第二个是返回参数 返回未Integer类型
      // 执行存储过程
      c.execute();
      value = c.getFloat(3);
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return value;
  }

  public String GetMainOrderStatus(String status) {
    // TODO Auto-generated method stub
    String OrderStatus = "";
    DBTable table = new DBTable(SQLCon.GetConnect(),
        "select isnull(eos_MainName,'') as eos_MainName from eos_OrderStatus where eos_name='" + status
            + "' and eos_status<>'D'");
    try {
      table.Open();
      if (table.next()) {
        OrderStatus = table.getString("eos_MainName");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return OrderStatus;
  }

  
  public JSONObject UpdatePostaddress(String OrderID, String UserName)
  {
    JSONObject addrJson=GetPostAddress(OrderID);
    if (addrJson.getInt("MsgID")!=1)
    {
      return WebFunction.WriteMsgToJson(-1,addrJson.getString("MsgText"));
    }
    String post_address=addrJson.getString("post_address");

    DBTable table = new DBTable(SQLCon.GetConnect(), "select Ebo_Addr,Ebo_id,Ebo_User_Lst,Ebo_Date_Lst from Ebo_BookingOrder where Ebo_SourceOrderNo='"+OrderID+"'");
    try {
      table.Open();
      while (table.next()) {
        if (!table.getString("Ebo_Addr").equals(post_address))
        {
          table.UpdateValue("Ebo_User_Lst", UserName);
          table.UpdateValue("Ebo_Date_Lst", new Date());
          table.UpdateValue("Ebo_Addr", post_address);
          table.PostRow();
        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return WebFunction.WriteMsgToJson(-1, e.getMessage());
    } finally {
      table.CloseAndFree();
    }
    return WebFunction.WriteMsgToJson(1,"Success");
  }

  public JSONObject UpdateMailNo(String OrderID,String MailNo, String UserName)
  {
    DBTable table = new DBTable(SQLCon.GetConnect(), "select Ebo_MailNo,Ebo_id,Ebo_User_Lst,Ebo_Date_Lst from Ebo_BookingOrder where Ebo_SourceOrderNo='"+OrderID+"'");
    try {
      table.Open();
      while (table.next()) {
        if (!table.getString("Ebo_MailNo").equals(MailNo))
        {
          table.UpdateValue("Ebo_User_Lst", UserName);
          table.UpdateDate("Ebo_Date_Lst", new Date().getTime());
          table.UpdateValue("Ebo_MailNo", MailNo);
          table.PostRow();
        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return WebFunction.WriteMsgToJson(-1, e.getMessage());
    } finally {
      table.CloseAndFree();
    }
    return WebFunction.WriteMsgToJson(1,"Success");
  }
  public String GetMailNo(String OrderID)
  {
    DBTable table = new DBTable(SQLCon.GetConnect(), "select isnull(Ebo_MailNo,Ebo_SourceOrderNo) as Ebo_MailNo from Ebo_BookingOrder where Ebo_SourceOrderNo='"+OrderID+"'");
    String Ebo_MailNo=OrderID;
    try {
      table.Open();
      if  (table.next()) {
        Ebo_MailNo=table.getString("Ebo_MailNo");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return Ebo_MailNo;
  }
  
  public String GetTypeHistory(String OrderNo,String Type)
  {

    DateFormat format = new SimpleDateFormat("MM-dd HH:mm");
    DBTable table = new DBTable(SQLCon.GetConnect(), "select Eboh_Date_op,Eboh_User_op,Eboh_remark,* from Eboh_BookingOrderHistory,Ebo_BookingOrder\r\n" + 
        "where Eboh_id_ebo=Ebo_id and eboh_type='"+Type+"' and Ebo_SourceOrderNo='"+OrderNo+"'\r\n" + 
        "order by Eboh_Date_Ins" );
    String result="";
    try {
      table.Open();
      while  (table.next()) {
        String Eboh_User_op=table.getString("Eboh_User_op");
        String Eboh_remark=table.getString("Eboh_remark");
        Date Eboh_Date_op=(Date)table.GetValue("Eboh_Date_op");
        result=result+Eboh_User_op+"【"+format.format(Eboh_Date_op)+"】："+Eboh_remark+"\r\n";
        
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
    return result;
  }
  public void SyncData(String SourceOrderNo) {
    try {
      MongoDatabase database = MongoCon.GetConnect();
      MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");
      FindIterable<Document> findIterable = collection
          .find(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", SourceOrderNo));
      MongoCursor<Document> mongoCursor = findIterable.iterator();
      if (mongoCursor.hasNext()) {
        Document document = mongoCursor.next();
        JSONObject alitrip_travel_trade_query_response = new JSONObject(document.toJson());

        JSONObject first_result = alitrip_travel_trade_query_response.getJSONObject("alitrip_travel_trade_query_response").getJSONObject("first_result");

        JSONObject sub_orders = first_result.getJSONObject("sub_orders");
        JSONArray sub_order_info = sub_orders.getJSONArray("sub_order_info");
        JSONObject contactor = sub_order_info.getJSONObject(0).getJSONObject("contactor");
        

        JSONObject buyer_info = first_result.getJSONObject("buyer_info");


        String ebo_SourceGuest=buyer_info.getString("buyer_nick");
        String ebo_LinkMan=contactor.getString("name");
        String ebo_Phone=contactor.getString("phone");

        
        String Ebo_Addr = contactor.getString("post_address");
        Ebo_Addr = Ebo_Addr.replaceAll("\\(|\\)", "");

        String Ebo_OrderState=first_result.getString("status");
        
        DBTable table =new DBTable(SQLCon.GetConnect(),"select * from Ebo_BookingOrder where ebo_sourceorderno='"+SourceOrderNo+"'");
        try {
          table.Open();
          if (table.next()) {
            System.out.println("Ebo_BookingOrder：同步信息");
            table.UpdateValue("Ebo_User_Lst", "SyncData");
            Long time=new Date().getTime();
            System.out.println("time:"+time);
            table.UpdateDate("Ebo_Date_Lst",time);
            
            table.UpdateValue("Ebo_Addr", Ebo_Addr);//更新寄件地址
            table.UpdateValue("Ebo_OrderState", Ebo_OrderState);//更新订单状态
            table.UpdateValue("Ebo_SourceGuest", ebo_SourceGuest);//更新订单状态
            table.UpdateValue("Ebo_LinkMan", ebo_LinkMan);//更新订单状态
            table.UpdateValue("Ebo_Phone", ebo_Phone);//更新订单状态
            if (Ebo_OrderState.equals("TRADE_CLOSED")) {
              table.UpdateValue("Ebo_StatusType", "已退款");//更新订单状态
            }
            table.PostRow();
          }
          else {
            System.out.println("Ebo_BookingOrder：无记录！！！");
          }
        }catch (SQLException e) {
          e.printStackTrace();
          // TODO: handle exception
        } finally {
          // TODO: handle finally clause
          table.CloseAndFree();
        }
      }
      else
      {

        System.out.println("Ebo_BookingOrder：无记录！！！");
      }
    } finally {
    }
    
    
  }
  
  public JSONObject GetPostAddress(String SourceOrderNo) {
    try {
      MongoDatabase database = MongoCon.GetConnect();
      MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");
      FindIterable<Document> findIterable = collection
          .find(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", SourceOrderNo));
      MongoCursor<Document> mongoCursor = findIterable.iterator();
      if (mongoCursor.hasNext()) {
        Document document = mongoCursor.next();
        JSONObject alitrip_travel_trade_query_response = new JSONObject(document.toJson());

        JSONObject first_result = alitrip_travel_trade_query_response.getJSONObject("alitrip_travel_trade_query_response").getJSONObject("first_result");

        JSONObject sub_orders = first_result.getJSONObject("sub_orders");
        JSONArray sub_order_info = sub_orders.getJSONArray("sub_order_info");
        JSONObject contactor = sub_order_info.getJSONObject(0).getJSONObject("contactor");

        
        String post_address = contactor.getString("post_address");
        post_address = post_address.replaceAll("\\(|\\)", "");
        contactor.put("post_address", post_address);
        contactor.put("MsgID", 1);
        return contactor;
      }
      else
      {
        return WebFunction.WriteMsgToJson(-1, "无订单记录（"+SourceOrderNo+"）");
      }
    } finally {
    }
  }


  public JSONObject OrderSendGoods(JSONObject JsonData) {
    // TODO Auto-generated method stub
    try {
      MongoDatabase database = MongoCon.GetConnect();
      MongoCollection<Document> collection = database.getCollection("OrderSendGoods");
      Document documents= Document.parse(JsonData.toString());      
      collection.insertOne(documents);      
      } 
    finally {
    }
    return WebFunction.WriteMsgToJson(1,"Success");
  }

  
  
}
