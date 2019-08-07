package com.ecity.java.mvc.service.visa.ota;

import java.sql.SQLException;

import org.bson.Document;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.uilts.BeanPostData;
import com.ecity.java.mvc.service.taobao.ota.TaoBaoOTAService;
import com.ecity.java.mvc.service.visa.base.OrderStatusService;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.taobao.api.ApiException;

public class BookingOrderNameListService implements IBookingOrderNameListService {

  @Override
  public JSONObject PostData(JSONArray data) {
    // TODO Auto-generated method stub
    BeanPostData bean = new BeanPostData("Ebon_BookingOrder_NameList", "_id", "Ebon", data);
    return bean.Post();
  }

  @Override
  public boolean UpdateApplyID(String id, String ApplyID) {
    // TODO Auto-generated method stub
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    boolean result = update
        .Execute("update Ebon_BookingOrder_NameList set ebon_user_lst='更新申请人',ebon_date_lst=getdate(),Ebon_ApplyId='"
            + ApplyID + "' where ebon_id=" + id);
    update.CloseAndFree();
    return result;

  }
  @Override
  public boolean UpdateApplyIDByCertNo(String EboID,String cert_no, String ApplyID) {
    // TODO Auto-generated method stub
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    boolean result = update
        .Execute("update Ebon_BookingOrder_NameList set ebon_user_lst='更新申请人',ebon_date_lst=getdate(),Ebon_ApplyId='"
            + ApplyID + "' where ebon_id_ebo=" + EboID +" and Ebon_passPortNo='"+cert_no+"'");
    update.CloseAndFree();
    return result;

  }

  @Override
  public boolean UpdateApplyIDByOrderID(String OrderNo) {
    // TODO Auto-generated method stub
    

    TaoBaoOTAService taobaoService = new TaoBaoOTAService();
    
    MongoDatabase database = MongoCon.GetConnect();
    MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");

    BasicDBObject query = new BasicDBObject();
    query.put("SrcOrderID", new BasicDBObject("$eq", OrderNo));
    query.put("MsgID", new BasicDBObject("$eq", "1"));

    FindIterable<Document> findIterable = collection.find(query);
    MongoCursor<Document> mongoCursor = findIterable.iterator();
    while (mongoCursor.hasNext()) {
      Document document = mongoCursor.next();
      String json = document.toJson();
      // System.out.println(json);

      JSONObject alitrip_travel_trade_query_response = new JSONObject(json);

      String OrderID=alitrip_travel_trade_query_response.getString("OrderID");
          
      JSONObject first_result = alitrip_travel_trade_query_response.getJSONObject("alitrip_travel_trade_query_response")
          .getJSONObject("first_result");
      JSONObject travellers = first_result.getJSONObject("sub_orders").getJSONArray("sub_order_info").getJSONObject(0)
          .getJSONObject("travellers");
      if (travellers.has("traveller_info")) {
        JSONArray traveller_info = travellers.getJSONArray("traveller_info");
        for (int i = 0; i < traveller_info.length(); i++) {
          JSONObject traveller_infos = traveller_info.getJSONObject(i);
          String credential_no=traveller_infos.getString("credential_no");
          
          
          String extend_attributes_json_str = traveller_infos.getString("extend_attributes_json");
          JSONObject extend_attributes_json = new JSONObject(extend_attributes_json_str);
          String applyId = extend_attributes_json.getString("applyId");
          String currentApplyStatus = extend_attributes_json.getString("currentApplyStatus");
          String nextApplyStatus = extend_attributes_json.getString("nextApplyStatus");          

          String state = taobaoService.GetVisaStateName(Integer.parseInt(currentApplyStatus));
          System.out.println("同步ApplyId:[credential_no="+credential_no+"][applyId="+applyId+"][OrderID="+OrderID+"]");
          if (!applyId.equals("")) {
            DBTable table=new DBTable(SQLCon.GetConnect());
            int EbonID=0;
            
            try {
              table.SQL("select Ebon_id from Ebon_BookingOrder_NameList where ebon_id_ebo="+OrderID+" and Ebon_passPortNo='" + credential_no + "'");
              table.Open();
              if (table.next())
              {
                EbonID=table.getInt("Ebon_id");
              }
              else {
                table.SQL("select Ebon_id from Ebon_BookingOrder_NameList where ebon_id_ebo="+OrderID+" and isnull(Ebon_ApplyId,'')=''");
                table.Open();
                if (table.next()) {
                  EbonID=table.getInt("Ebon_id");
                }
              }              
            } catch (SQLException e) {
              // TODO Auto-generated catch block              
              e.printStackTrace();
            }
            finally {
              table.CloseAndFree();
            }
            
            if (EbonID!=0)
            {
              DBQuery update = new DBQuery(SQLCon.GetConnect());
              boolean result = update.Execute(
                  "update Ebon_BookingOrder_NameList set ebon_user_lst='自动同步',ebon_date_lst=getdate(),Ebon_StatusType='"
                      + state + "',Ebon_currentApplyStatus='" + currentApplyStatus + "',Ebon_nextApplyStatus='"
                      + nextApplyStatus + "',Ebon_ApplyId='"+applyId+"',Ebon_passPortNo='"+credential_no+"' where Ebon_id="+EbonID);
              update.CloseAndFree();
            }
          }
          //bookingOrderService.UpdateStatusByCode(OrderNo, "自动同步", state, "");
        }
      }
    }
    return true;

  }

  @Override
  public JSONObject  UpdateVisaApplicantInfo(String OrderNo,String OrderID)
  {
    TaoBaoOTAService taobaoService = new TaoBaoOTAService();
    JSONObject ResultJson=null;
    JSONArray updateJson=null;
    BookingOrderNameListService nameService = new BookingOrderNameListService();
    try {
      updateJson = taobaoService.UpdateVisaApplicantInfo(OrderNo);
    } catch (ApiException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return WebFunction.WriteMsgToJson(-1, "申请人信息更新失败！");
    }
    for (int j =0;j<updateJson.length();j++)
    {
      ResultJson=updateJson.getJSONObject(j);
      if (ResultJson.O("alitrip_travel_visa_applicant_update_response").getBoolean("update_result")) {
        // {"alitrip_travel_visa_applicant_update_response":{"applicants":{"normal_visa_applicant_info":[{"cert_no":"G50188590","apply_id":"2306987_1"},{"cert_no":"E30567039","apply_id":"2306987_2"}]},"update_result":true,"request_id":"8dc5vqv1w7eo"}}
        JSONArray normal_visa_applicant_info = ResultJson.O("alitrip_travel_visa_applicant_update_response")
            .O("applicants").A("normal_visa_applicant_info");
        for (int i = 0; i < normal_visa_applicant_info.length(); i++) {
          String apply_id = normal_visa_applicant_info.getJSONObject(i).getString("apply_id");
          String cert_no = normal_visa_applicant_info.getJSONObject(i).getString("cert_no");
          
//          if (rows.length() > i) {
//            String id = rows.getJSONObject(i).getString("_id");
            System.out.println("OrderID:"+OrderID+";cert_no:" + cert_no + ";apply_id:" + apply_id);
            if (!nameService.UpdateApplyIDByCertNo(OrderID,cert_no, apply_id)) {
              return WebFunction.WriteMsgToJson(-1, "申请人信息更新失败！");
            }
//          }
        }
      }
    }
    return WebFunction.WriteMsgToJson(1, "申请人信息更新成功！");
  }
  
  
  @Override
  public boolean UpdateApplictionStateByApplyID(String applyId, String state) {
    // TODO Auto-generated method stub
    DBQuery update = new DBQuery(SQLCon.GetConnect());
    boolean result = update
        .Execute("update Ebon_BookingOrder_NameList set ebon_user_lst='更新状态',ebon_date_lst=getdate(),Ebon_StatusType='"
            + state + "',Ebon_StatusOTA='" + state + "',Ebon_StatusOTA2=CONVERT(nvarchar(10),getdate(),112)+'"+state+"' where Ebon_ApplyId='" + applyId + "'");

    update.CloseAndFree();
    return result;
  }

  @Override
  public JSONObject UpdateApplyState(String OrderNo) {
    // TODO Auto-generated method stub

    System.out.println("UpdateApplyState:" + OrderNo);
    TaoBaoOTAService taobaoService = new TaoBaoOTAService();
    BookingOrderService bookingOrderService = new BookingOrderService();

    MongoDatabase database = MongoCon.GetConnect();
    MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");

    BasicDBObject query = new BasicDBObject();
    query.put("SrcOrderID", new BasicDBObject("$eq", OrderNo));
    query.put("MsgID", new BasicDBObject("$eq", "1"));

    FindIterable<Document> findIterable = collection.find(query);
    MongoCursor<Document> mongoCursor = findIterable.iterator();
    while (mongoCursor.hasNext()) {
      Document document = mongoCursor.next();
      String json = document.toJson();
      // System.out.println(json);

      JSONObject alitrip_travel_trade_query_response = new JSONObject(json);
      JSONObject first_result = alitrip_travel_trade_query_response.getJSONObject("alitrip_travel_trade_query_response")
          .getJSONObject("first_result");
      JSONObject travellers = first_result.getJSONObject("sub_orders").getJSONArray("sub_order_info").getJSONObject(0)
          .getJSONObject("travellers");
      if (travellers.has("traveller_info")) {
        JSONArray traveller_info = travellers.getJSONArray("traveller_info");
        for (int i = 0; i < traveller_info.length(); i++) {
          JSONObject traveller_infos = traveller_info.getJSONObject(i);
          String extend_attributes_json_str = traveller_infos.getString("extend_attributes_json");
          JSONObject extend_attributes_json = new JSONObject(extend_attributes_json_str);
          String applyId = extend_attributes_json.getString("applyId");
          String currentApplyStatus = extend_attributes_json.getString("currentApplyStatus");
          String nextApplyStatus = extend_attributes_json.getString("nextApplyStatus");

          String state = taobaoService.GetVisaStateName(Integer.parseInt(currentApplyStatus));
          if (!applyId.equals("")) {
            DBQuery update = new DBQuery(SQLCon.GetConnect());
            boolean result = update.Execute(
                "update Ebon_BookingOrder_NameList set ebon_user_lst='自动同步',ebon_date_lst=getdate(),Ebon_StatusType='"
                    + state + "',Ebon_currentApplyStatus='" + currentApplyStatus + "',Ebon_nextApplyStatus='"
                    + nextApplyStatus + "' where Ebon_ApplyId='" + applyId + "'");
            update.CloseAndFree();
          }

          bookingOrderService.UpdateStatusByCode(OrderNo, "自动同步", state, "");
        }
      }
    }
    return WebFunction.WriteMsgToJson(1, "Success!");
  }
  @Override
  public JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String alitripCode,String currentApplyStatus,String PostNumber,String PostCompanyCode,
      String PostCompanyName,String FileName,String BookFileName,String BookTime,String BookPlace)
  {

    OrderStatusService orderStatusService = new OrderStatusService();

    TaoBaoOTAService service = new TaoBaoOTAService();
    JSONObject Result=new JSONObject();
    try {

      String state = orderStatusService.GetState(alitripCode);
      System.out.println("state:" + state);
      int index = state.indexOf(currentApplyStatus);

      System.out.println("currentApplyStatus:" + currentApplyStatus);
      System.out.println("index:" + index);
      if ((index > -1) &&(state.length()>index)) {
        index = index + currentApplyStatus.length() + 1;
        if (state.length()>index) {
           
          String stateTemp = state.substring(index);
  
          System.out.println("stateTemp:" + stateTemp);
          String[] stateList = stateTemp.split(",");
          for (int j = 0; j < stateList.length; j++) {
            String currentStatus = stateList[j];
            System.out.println("currentStatus:" + currentStatus);
            service.TravelvisaApplicantUpdate(OrderNo, ApplyID, stateList[j],PostNumber,PostCompanyCode,
                PostCompanyName,FileName,BookFileName,BookTime,BookPlace);
          } 
        }
      }
      Result.put("MsgID", 1);
      Result.put("MsgTest", "Success!");
    } catch (ApiException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      Result.put("MsgID", -1);
      Result.put("MsgTest", e.getMessage());
    }

    return Result;
  }
  
  public int GetVisaType(int Ebon_id)
  {
    DBTable table=new DBTable(SQLCon.GetConnect(),"select Epi_id_Evt from Ebon_BookingOrder_NameList,Ebop_BookingOrder_Package,Epi_Product_Info\r\n" + 
        "where ebon_id=ebop_id_ebon and ebop_id_epi=epi_id\r\n" + 
        "and ebon_id="+Ebon_id);
    try {
      table.Open();
      if (table.next())
      {
        return table.getInt("Epi_id_Evt");
      }
      else {
        return 0;
      }
      
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      // : handle finally clause
      table.CloseAndFree();
    }
    return 0;
  }
}
