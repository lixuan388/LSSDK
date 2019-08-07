package com.ecity.java.web.taobao.api;

import java.util.Date;

import org.bson.Document;

import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.service.visa.ota.BookingOrderNameListService;
import com.ecity.java.mvc.service.visa.ota.BookingOrderService;
import com.ecity.java.web.taobao.service.TaobaoService;
import com.java.sql.MongoCon;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.taobao.api.ApiException;
import com.taobao.api.request.AlitripTravelTradeQueryRequest;
import com.taobao.api.response.AlitripTravelTradeQueryResponse;

public class alitripTravelTradeQueryClass {

  public static JSONObject OrderInfo(String OrderID, boolean IsUpdate) {
    // TODO Auto-generated method stub
    try {

      JSONObject ReturnJson = null;
      System.out.println("--alitripTravelTradeQueryServlet--");

      System.out.println("OrderID:" + OrderID);
      MongoDatabase database = MongoCon.GetConnect();

      MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");

      FindIterable<Document> findIterable = collection
          .find(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID));
      MongoCursor<Document> mongoCursor = findIterable.iterator();

      BookingOrderNameListService NameListService = new BookingOrderNameListService();
      BookingOrderService bookingOrderService = new BookingOrderService();

      if (mongoCursor.hasNext()) {
        Document document = mongoCursor.next();

        System.out.println("记录已存在");
        if (!IsUpdate) {
          ReturnJson = new JSONObject(document.toJson());
          return ReturnJson;
        } else {
          System.out.println("更新记录");
          String JsonStr = GetOrderInfo(OrderID);
          ReturnJson = new JSONObject(JsonStr);
          Document document2 = Document.parse(JsonStr);

          document2.put("Update", new Date());
          java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          String date = format1.format(new Date());
          document2.put("UpdateDateStr", date);

          collection.updateMany(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID),
              new Document("$set", document2), new UpdateOptions().upsert(true));

          NameListService.UpdateApplyState(OrderID);
          bookingOrderService.SyncData(OrderID);
          
        }

      } else {
        System.out.println("新记录");

        String JsonStr = GetOrderInfo(OrderID);
        ReturnJson = new JSONObject(JsonStr);
        if (ReturnJson.has("error_response")) {
          System.out.println(ReturnJson.toString());
          return null;
        }

        Document document = Document.parse(JsonStr);
        document.put("SrcOrderID", OrderID);
        document.put("MsgID", "0");
        document.put("InsDate", new Date());

        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = format1.format(new Date());
        document.put("InsDateStr", date);

        JSONObject first_result = ReturnJson.getJSONObject("alitrip_travel_trade_query_response")
            .getJSONObject("first_result");
        String out_product_id = first_result.getJSONObject("sub_orders").getJSONArray("sub_order_info").getJSONObject(0)
            .getJSONObject("buy_item_info").getString("out_product_id");
        String out_sku_id = first_result.getJSONObject("sub_orders").getJSONArray("sub_order_info").getJSONObject(0)
            .getJSONObject("buy_item_info").getString("out_sku_id");

        if (!out_product_id.equals("")) {
          out_sku_id = out_product_id;
        }
        document.put("out_sku_id", out_sku_id);

        collection.updateMany(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", OrderID),
            new Document("$set", document), new UpdateOptions().upsert(true));
        NameListService.UpdateApplyState(OrderID);
        bookingOrderService.SyncData(OrderID);
        
      }
      return ReturnJson;
    }catch (Exception e) {      
      // TODO: handle exception      
      e.printStackTrace();
      return null;
    } finally {
      System.out.println("--alitripTravelTradeQueryServlet end --");
    }

  }

  public static String GetOrderInfo(String OrderID) {
    AlitripTravelTradeQueryRequest req = new AlitripTravelTradeQueryRequest();
    req.setOrderId(Long.valueOf(OrderID));
    AlitripTravelTradeQueryResponse rsp = null;
    try {
      rsp = TaobaoService.Client().execute(req, TaobaoService.Sessionkey);
    } catch (ApiException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(rsp.getBody());
    return rsp.getBody();
  }

}
