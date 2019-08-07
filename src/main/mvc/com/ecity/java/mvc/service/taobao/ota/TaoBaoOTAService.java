package com.ecity.java.mvc.service.taobao.ota;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.visa.ota.BookingOrderNameListImpl;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;
import com.ecity.java.mvc.service.visa.ota.BookingOrderService;
import com.ecity.java.sql.db.DBQuery;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.ecity.java.web.taobao.service.TaobaoService;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.taobao.api.ApiException;
import com.taobao.api.request.AlitripTravelVisaApplicantUpdateRequest.NormalVisaApplicantInfo;
import com.taobao.api.request.AlitripTravelVisaApplicantUpdateRequest.NormalVisaApplicantOperation;
import com.taobao.api.request.AlitripTravelVisaApplicantUpdateRequest.NormalVisaAppointmentInfo;
import com.taobao.api.request.AlitripTravelVisaApplicantUpdateRequest.NormalVisaEtaInfo;
import com.taobao.api.request.AlitripTravelVisaApplicantUpdateRequest.NormalVisaLogisticsInfo;

public class TaoBaoOTAService extends TaobaoService implements ITaoBaoOTAService {

  @Override
  public JSONObject TravelTradeQuery(String OrderID, boolean IsUpdate) throws ApiException {
    try {
      JSONObject ReturnJson = null;
      System.out.println("--------------------alitripTravelTradeQueryServlet--------------------");

      System.out.println("OrderID:" + OrderID);
      MongoDatabase database = MongoCon.GetConnect();

      MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");

      FindIterable<Document> findIterable = collection
          .find(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID));
      MongoCursor<Document> mongoCursor = findIterable.iterator();

      if (mongoCursor.hasNext()) {
        Document document = mongoCursor.next();

        System.out.println("记录已存在");
        if (!IsUpdate) {
          ReturnJson = new JSONObject(document.toJson());
          return ReturnJson;
        } else {
          System.out.println("更新记录");
          TaobaoService service = new TaobaoService();

          ReturnJson = service.alitripTravelTradeQuery(OrderID);
          String JsonStr = ReturnJson.toString();
          Document document2 = Document.parse(JsonStr);
          collection.updateMany(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID),
              new Document("$set", document2), new UpdateOptions().upsert(true));
        }
      } else {
        System.out.println("新记录");

        ReturnJson = this.alitripTravelTradeQuery(OrderID);

        Document document = Document.parse(ReturnJson.toString());
        document.put("SrcOrderID", OrderID);
        document.put("MsgID", "0");
        document.put("InsDate", new Date());
        collection.updateMany(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID),
            new Document("$set", document), new UpdateOptions().upsert(true));

      }
      return ReturnJson;
    } finally {
      System.out.println("--------------------alitripTravelTradeQueryServlet end --------------------");
    }
  }

  @Override
  public JSONArray UpdateVisaApplicantInfo(String OrderNo) throws ApiException {
    // TODO Auto-generated method stub
    System.out.println("---------UpdateVisaApplicantInfo begin ---------");
    BookingOrderNameListImpl dao = new BookingOrderNameListImpl();
    List<BookingOrderNameListPO> NameList = dao.findByOrderNo(OrderNo);

    TaobaoService taobaoService = new TaobaoService();
//    Long OperType = 1l;// 操作类型。1-上传新申请人基本信息（商家代填申请人），2-更新已有申请人基本信息，3-更新已有申请人的签证进度

    JSONArray result=new JSONArray();
    for (int i = 0; i < NameList.size(); i++) {
      List<NormalVisaApplicantInfo> list2 = new ArrayList<NormalVisaApplicantInfo>();

      Long OperType = 1l;// 操作类型。1-上传新申请人基本信息（商家代填申请人），2-更新已有申请人基本信息，3-更新已有申请人的签证进度
      BookingOrderNameListPO name = NameList.get(i);
      String applyId = name.get_ApplyId()==null?"":name.get_ApplyId();

      String lastName_e = name.get_lastname_e();
      String firstName_e = name.get_firstname_e();
      String passPortNo = name.get_PassPort();
      String mobile = name.get_mobile();
      if (!applyId.equals("")) {
        OperType = 2l;
      }

      NormalVisaApplicantInfo obj3 = new NormalVisaApplicantInfo();
      obj3.setApplyId(applyId);
      obj3.setSurname(lastName_e);
      obj3.setGivenName(firstName_e);
      obj3.setMobile(mobile);
      obj3.setCertNo(passPortNo);

      System.out.println("--index:" + i + "--");
      System.out.println("getApplyId:" + obj3.getApplyId());
      System.out.println("getSurname:" + obj3.getSurname());
      System.out.println("getGivenName:" + obj3.getGivenName());
      System.out.println("getMobile:" + obj3.getMobile());
      System.out.println("getCertNo:" + obj3.getCertNo());

      list2.add(obj3);
      JSONObject json = taobaoService.alitripTravelvisaApplicantUpdate(OrderNo, OperType, list2, null);
      System.out.println(json);
      UpdateApplyID(OrderNo, json);
      result.put(json);
      
    }
//		System.out.println("list2.toString():"+list2.toString());

    System.out.println("---------UpdateVisaApplicantInfo end ---------");
    return result;
  }

  
  public void UpdateApplyID(String OrderNo,JSONObject  response) {
    if (response.has("alitrip_travel_visa_applicant_update_response")) {
      JSONArray array=response.getJSONObject("alitrip_travel_visa_applicant_update_response").getJSONObject("applicants").getJSONArray("normal_visa_applicant_info");

      BookingOrderService booking =new BookingOrderService();
      int ebo_id=booking.GetOrderIDByCode(OrderNo);
      DBQuery update =new DBQuery(SQLCon.GetConnect());
      for (int i =0;i<array.length();i++) {
        JSONObject  info=array.getJSONObject(i);
        String cert_no=info.getString("cert_no");
        String apply_id=info.getString("apply_id");
        update.Execute("update Ebon_BookingOrder_NameList set ebon_applyID='"+apply_id+"',ebon_date_lst=getdate(),ebon_user_lst='更新apply_id' where ebon_PassPort='"+cert_no+"' and ebon_id_ebo="+ebo_id);
      }
      update.CloseAndFree();
      
    }
    
  }
  @Override
  public JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String state) throws ApiException {
    // TODO Auto-generated method stub
    return TravelvisaApplicantUpdate(OrderNo, ApplyID, state,"","",
       "","","","","");
  }

  public JSONObject TravelvisaApplicantUpdate_HasPost(String OrderNo, String ApplyID, String state,String PostNumber) throws ApiException {
    // TODO Auto-generated method stub
    return TravelvisaApplicantUpdate(OrderNo, ApplyID, state,PostNumber,"SF","顺风快递","","","","");
  }

  @Override
  public JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String state,String PostNumber,String PostCompanyCode,
      String PostCompanyName,String FileName,String BookFileName,String BookTime,String BookPlace) throws ApiException {
    // TODO Auto-generated method stub
//    BookingOrderNameListImpl dao=new BookingOrderNameListImpl();
//    List<BookingOrderNameList> NameList =dao.findByOrderNo(OrderNo);

    TaobaoService taobaoService = new TaobaoService();

    NormalVisaApplicantOperation obj4 = new NormalVisaApplicantOperation();
    obj4.setApplyId(ApplyID);
    obj4.setStatus(Long.parseLong(state));
    obj4.setRemark("更新签证状态");
    if (!PostNumber.equals(""))
    {
      NormalVisaLogisticsInfo obj5 = new NormalVisaLogisticsInfo();
      obj5.setPostNumber(PostNumber);
      obj5.setPostCompanyCode(PostCompanyCode);
      obj5.setPostCompanyName(PostCompanyName);
      obj4.setLogisticsInfo(obj5);
//      System.out.println("推送物流信息");
//      System.out.println("PostNumber："+PostNumber);
//      System.out.println("PostCompanyCode："+PostCompanyCode);
//      System.out.println("PostCompanyName:"+PostCompanyName);
    }
    if (!FileName.equals(""))
    {
      NormalVisaEtaInfo obj6 = new NormalVisaEtaInfo();
      obj6.setFileName("电子签证结果");
      obj4.setEtaInfo(obj6);
    }
    if (!BookFileName.equals(""))
    {
      NormalVisaAppointmentInfo obj7 = new NormalVisaAppointmentInfo();
      obj7.setBookFileName("预约面试信");
      obj7.setBookTime("2018-01-01 10:00:00");
      obj7.setBookPlace("预约地点");
      obj4.setAppointmentInfo(obj7);
    }



    JSONObject json = taobaoService.alitripTravelvisaApplicantUpdate(OrderNo, 3l, null, obj4);
    json.put("MsgID", 1);
    json.put("MsgText", "Success");
    System.out.println(json);
    return json;
  }
  public String GetOrderStatusText(String code) {
    DBTable table=new DBTable(SQLCon.GetConnect(),"select eos_state from eos_OrderStatus  where eos_code='"+code+"'");
    try {
      table.Open();
      if (table.next()) {
         return table.getString("eos_state");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    finally {
      // TODO: handle finally clause
      table.CloseAndFree();
    }
    return "";
    
  }

}
