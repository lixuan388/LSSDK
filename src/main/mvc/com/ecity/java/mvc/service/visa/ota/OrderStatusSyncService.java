package com.ecity.java.mvc.service.visa.ota;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.visa.ota.BookingOrderNameListImpl;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;
import com.ecity.java.mvc.service.taobao.ota.TaoBaoOTAService;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.ecity.java.web.taobao.api.alitripTravelTradeQueryClass;
import com.ecity.java.web.taobao.service.TaobaoService;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.taobao.api.ApiException;

public class OrderStatusSyncService {
  
  public void Synchronization() {
    DBTable table =new DBTable(SQLCon.GetConnect(),"select ebon_nextapplystatus,eos_state,ava_statustype,eos_code,eos_mainName,ebon_id,ebon_statustype,ebon_name,ebo_packagename,ebo_sourceorderno,ebo_statustype,\r\n" + 
        "eos_Name,ebo_id,ebon_applyid,ebon_currentApplyStatus\r\n" + 
        "from Ebon_BookingOrder_NameList ,ava_visa_application,Ebo_BookingOrder,eos_orderstatus\r\n" + 
        "where ebon_id_ava=ava_id and ebon_id_ebo=ebo_id\r\n" + 
        "and eos_ERPName=ava_statustype\r\n" + 
        "and ebo_statustype not in ('已取消')\r\n" + 
        "--and ebo_statustype in ('未完成')\r\n" + 
        "--and ebon_statustype<>eos_Name\r\n" + 
        "and ebon_applyid<>'' and Ebon_currentApplyStatus<>'' and ebon_nextapplystatus<>'[]'  and ebon_nextapplystatus<>''\r\n"
        + "and (isnull(Ebon_SyncFlag,0)=0 or (isnull(Ebon_SyncFlag,0)=1  and Ebon_SyncDate-0.5>getdate()) )"
        + "and Ebon_SyncDate<getdate()" + 
        "order by Ebo_BookingOrder.ebo_paydate desc,Ebo_BookingOrder.ebo_sourceorderno,ava_visa_application.ava_statustype,Ebon_SyncDate");
    try {
      table.Open();

      //TaoBaoOTAService taobaoService = new TaoBaoOTAService();
      
      while (table.next()) {
        System.out.println("-----------------------------");
        int ebon_id=table.getInt("ebon_id");
        System.out.println("ebon_id:"+ebon_id);
        String ebon_nextapplystatus=table.getString("ebon_nextapplystatus");
        String eos_state=table.getString("eos_state");
        String eos_Name=table.getString("eos_Name");
        String ebon_applyid=table.getString("ebon_applyid");
        String ebon_currentApplyStatus=table.getString("ebon_currentApplyStatus");
        String ebon_name=table.getString("ebon_name");
        String ebo_packagename=table.getString("ebo_packagename");
        String ebo_sourceorderno=table.getString("ebo_sourceorderno");
        int ebo_id=table.getInt("ebo_id");
        
        JSONArray ebon_nextapplystatusJson=new JSONArray(ebon_nextapplystatus);
        for (int i =0;i<ebon_nextapplystatusJson.length();i++) {
          String nextapplystatus=""+ebon_nextapplystatusJson.getInt(i);
          if (eos_state.indexOf(nextapplystatus)>0) {
            System.out.println("ebon_nextapplystatus:"+ebon_nextapplystatus);
            System.out.println("eos_state:"+eos_state);
            System.out.println("nextapplystatus:"+nextapplystatus);
            UpdateState(ebon_id,ebo_id,ebon_applyid,ebon_name,ebo_packagename,ebo_sourceorderno,ebon_currentApplyStatus,nextapplystatus);
          }
        }
      }
      ReloadOrderInfo();
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
  }
  public JSONObject UpdateState(int ebon_id,int ebo_id,String ebon_applyid,String ebon_name,String ebo_packagename,String ebo_sourceorderno,String ebon_currentApplyStatus,String nextapplystatus) {
    if (nextapplystatus.equals("1013"))
    {
      BookingOrderNameListService svc=new BookingOrderNameListService();
      int VisaType=svc.GetVisaType(ebon_id);
      System.out.println("VisaType:"+VisaType);
      //已寄回结果，贴签，需要快递单号
      if (VisaType==1)
      {
        try {      
          TaoBaoOTAService taobaoService = new TaoBaoOTAService();          
          BookingOrderService bookingOrder=new BookingOrderService();
          String MailNo=bookingOrder.GetMailNo(ebo_sourceorderno);
          
          JSONObject ResultJson = taobaoService.TravelvisaApplicantUpdate_HasPost(ebo_sourceorderno, ebon_applyid, nextapplystatus,MailNo);
          System.out.println("UpdateState");
          System.out.println(ResultJson.toString());
          
          SyncLog(ebon_id,ebo_id,ebon_applyid,ebon_name,ebo_packagename,ebo_sourceorderno,ebon_currentApplyStatus,nextapplystatus,ResultJson);
          return ResultJson;
        } catch (ApiException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    try {      
      TaoBaoOTAService taobaoService = new TaoBaoOTAService();
      JSONObject ResultJson = taobaoService.TravelvisaApplicantUpdate(ebo_sourceorderno, ebon_applyid, nextapplystatus);

      System.out.println("UpdateState");
      System.out.println(ResultJson.toString());
      
      SyncLog(ebon_id,ebo_id,ebon_applyid,ebon_name,ebo_packagename,ebo_sourceorderno,ebon_currentApplyStatus,nextapplystatus,ResultJson);
      return ResultJson;
    } catch (ApiException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  public void SyncLog(int ebon_id,int ebo_id,String ebon_applyid,String ebon_name,String ebo_packagename,String ebo_sourceorderno,String ebon_currentApplyStatus,String nextapplystatus,JSONObject ResultJson) {
    MongoDatabase database = MongoCon.GetConnect();

    MongoCollection<Document> collection = database.getCollection("OrderStatusSyncLog");

    JSONObject Data=new JSONObject();
    Data.put("ebon_id", ebon_id);
    Data.put("ebo_id", ebo_id);
    Data.put("ebon_applyid", ebon_applyid);
    Data.put("ebon_name", ebon_name);
    Data.put("ebo_packagename", ebo_packagename);
    Data.put("ebo_sourceorderno", ebo_sourceorderno);
    Data.put("ebon_currentApplyStatus", ebon_currentApplyStatus);
    Data.put("nextapplystatus", nextapplystatus);
    Data.put("SyncDate",WebFunction.FormatDate(WebFunction.Format_YYYYMMDDHHMMSS,new Date()));
    Data.put("ResultJson", ResultJson);
    
    
    String Ebon_SyncFlag="";
    if (ResultJson.getString("error_response").equals("")) {
      Data.put("Result","Success");
      Ebon_SyncFlag=",Ebon_SyncFlag=0";
    }
    else {
      Data.put("Result","Error");
      Ebon_SyncFlag=",Ebon_SyncFlag=1";
    }

    String JsonStr = Data.toString();
    Document document = Document.parse(JsonStr);
    collection.insertOne(document);
    
    DBQuery update=new DBQuery(SQLCon.GetConnect());

    TaoBaoOTAService taobaoService = new TaoBaoOTAService();
    String state=taobaoService.GetVisaStateName(Integer.parseInt(nextapplystatus));
    update.Execute("update Ebon_BookingOrder_NameList set Ebon_SyncState='"+nextapplystatus+"'"+Ebon_SyncFlag+",Ebon_SyncDate=getdate(),Ebon_StatusOTA='" + state + "',Ebon_StatusOTA2=CONVERT(nvarchar(10),getdate(),112)+'"+state+"'  where ebon_id="+ebon_id);
    update.CloseAndFree();
  }

  public void ReloadOrderInfo(){
    DBTable table =new DBTable(SQLCon.GetConnect(),"select ebo_sourceorderno \r\n" + 
        "from Ebon_BookingOrder_NameList,Ebo_BookingOrder\r\n" + 
        "where ebon_id_ebo=ebo_id and  Ebon_SyncDate>getdate()-0.01\r\n" + 
        "group by ebo_sourceorderno");
    try {
      table.Open();
      while (table.next())
      {
        String OrderID=table.getString("ebo_sourceorderno");
        alitripTravelTradeQueryClass.OrderInfo(OrderID, true);
      }
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
  }

  public void ReloadOrderInfo(String OrderID){
    alitripTravelTradeQueryClass.OrderInfo(OrderID, true);
  }
  
  

  
//  public void UpdateState(String ebonid) {
//    DBTable table =new DBTable(SQLConnect.GetConnect(),"select ebon_id,ebon_nextapplystatus,ebon_applyid,ebon_currentApplyStatus,\r\n" + 
//        "ebon_name,ebo_packagename,ebo_sourceorderno,ebo_id\r\n" + 
//        "from Ebon_BookingOrder_NameList,Ebo_BookingOrder\r\n" + 
//        "where ebon_id_ebo=ebo_id and ebon_id='"+ebonid+"'");
//    try {
//      table.Open();
//
//      //TaoBaoOTAService taobaoService = new TaoBaoOTAService();
//      
//      if (table.next()) {
//        ebon_id=table.getInt("ebon_id");
//        ebon_nextapplystatus=table.getString("ebon_nextapplystatus");
//        ebon_applyid=table.getString("ebon_applyid");
//        ebon_currentApplyStatus=table.getString("ebon_currentApplyStatus");
//        ebon_name=table.getString("ebon_name");
//        ebo_packagename=table.getString("ebo_packagename");
//        ebo_sourceorderno=table.getString("ebo_sourceorderno");
//        ebo_id=table.getInt("ebo_id");
//        
//
//      }
//      else {
//        ebon_id=0;
//        ebon_nextapplystatus="";
//        ebon_applyid="";
//        ebon_currentApplyStatus="";
//        ebon_name="";
//        ebo_packagename="";
//        ebo_sourceorderno="";
//        ebo_id=0;
//      }
//
//    }catch (SQLException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }finally {
//      // TODO: handle finally clause
//      table.CloseAndFree();
//    }
//  }
  
  public void UpdateNameNotApplyID(JSONArray NameList) {

    for (int j=0;j<NameList.length();j++) {        
      JSONObject NameJson=NameList.getJSONObject(j);
      
      String _ApplyId=NameJson.getString("_ApplyId");
      if (_ApplyId.equals("")) {

        String _Name=NameJson.getString("_Name");
        String _name_e=NameJson.getString("_name_e");
        String[] NameE= _name_e.split(" ");

        System.out.println("_name_e:"+_name_e);
        
        String lastName_e = NameE[0];
        String firstName_e = NameE[1];
        String passPortNo = NameJson.getString("_passPortNo");
        String _id=NameJson.getString("_id");
        
        System.out.println("lastName_e:"+lastName_e);
        System.out.println("firstName_e:"+firstName_e);
        System.out.println("passPortNo:"+passPortNo);
        
        DBQuery update=new DBQuery(SQLCon.GetConnect());
        update.Execute("update Ebon_BookingOrder_NameList set ebon_name='"+_Name+"', ebon_lastname_e='"+lastName_e+"',ebon_firstname_e='"+firstName_e+"',ebon_PassPort='"+passPortNo+"' ,ebon_passportno='"+passPortNo+"' where ebon_id="+_id);
        update.CloseAndFree();
        
      }
    }
  }
  

  public Boolean UPdateStateByOrderID( String ebonid,String StateCode) {
    DBTable table =new DBTable(SQLCon.GetConnect(),"select ebon_id,ebon_nextapplystatus,ebon_applyid,ebon_currentApplyStatus,\r\n" + 
        "ebon_name,ebo_packagename,ebo_sourceorderno,ebo_id\r\n" + 
        "from Ebon_BookingOrder_NameList,Ebo_BookingOrder\r\n" + 
        "where ebon_id_ebo=ebo_id and ebon_id='"+ebonid+"'");
    
    Boolean Result=false;
    try {
      table.Open();

      //TaoBaoOTAService taobaoService = new TaoBaoOTAService();
      
      while (table.next()) {
        System.out.println("-----------------------------");
        int ebon_id=table.getInt("ebon_id");
        System.out.println("ebon_id:"+ebon_id);
        String ebon_nextapplystatus=table.getString("ebon_nextapplystatus");
        String ebon_applyid=table.getString("ebon_applyid");
        String ebon_currentApplyStatus=table.getString("ebon_currentApplyStatus");
        String ebon_name=table.getString("ebon_name");
        String ebo_packagename=table.getString("ebo_packagename");
        String ebo_sourceorderno=table.getString("ebo_sourceorderno");
        int ebo_id=table.getInt("ebo_id");
        
        JSONArray ebon_nextapplystatusJson=new JSONArray(ebon_nextapplystatus);
        for (int i =0;i<ebon_nextapplystatusJson.length();i++) {
          String nextapplystatus=""+ebon_nextapplystatusJson.getInt(i);
          if (StateCode.indexOf(nextapplystatus)>0) {
            System.out.println("ebon_nextapplystatus:"+ebon_nextapplystatus);
            System.out.println("nextapplystatus:"+nextapplystatus);
            
            JSONObject updateResult=UpdateState(ebon_id,ebo_id,ebon_applyid,ebon_name,ebo_packagename,ebo_sourceorderno,ebon_currentApplyStatus,nextapplystatus);
            System.out.println(updateResult.toString());
            if (!updateResult.has("error_response")) {
              Result=true;
            }
            //Result=false;
          }
        }
      }
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
    return Result;
  }

}
