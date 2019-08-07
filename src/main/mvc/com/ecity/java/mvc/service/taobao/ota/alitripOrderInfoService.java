package com.ecity.java.mvc.service.taobao.ota;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.uilts.SQLUilts;
import com.ecity.java.mvc.dao.visa.ota.ProductPackageDBODao;
import com.ecity.java.mvc.dao.visa.ota.ProductPackageMDao;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;
import com.ecity.java.mvc.model.visa.ota.ProductPackageDBO;
import com.ecity.java.mvc.model.visa.ota.ProductPackageMPO;
import com.ecity.java.mvc.service.taobao.ota.TaoBaoOTAService;
import com.ecity.java.sql.table.MySQLTable;
import com.java.sql.MongoCon;
import com.java.sql.SQLCon;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class alitripOrderInfoService {
  public String ID;
  
  private int Ebo_id_Eva=0;

  public alitripOrderInfoService(String ID) {
    this.ID = ID;
  }

  public JSONObject OpenTable() {

    JSONObject ReturnJson = new JSONObject();

//		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat df = new DecimalFormat("0.00");

    try {
      MongoDatabase database = MongoCon.GetConnect();
      MongoCollection<Document> collection = database.getCollection("alitripTravelTrade");
      FindIterable<Document> findIterable = collection
          .find(Filters.eq("alitrip_travel_trade_query_response.first_result.order_id_string", ID));
      MongoCursor<Document> mongoCursor = findIterable.iterator();
      if (mongoCursor.hasNext()) {
//				System.out.println("mongoCursor.hasNext()");
        Document document = mongoCursor.next();
//				System.out.println(document.toJson());

        JSONObject alitrip_travel_trade_query_response = new JSONObject(document.toJson());

        ReturnJson.put("MsgID", "1");
        ReturnJson.put("MsgText", "Success");

//				System.out.println("MsgID:"+alitrip_travel_trade_query_response.getString("MsgID"));
        if (alitrip_travel_trade_query_response.getString("MsgID").equals("1")) {
          ReturnJson.put("MsgID", "-11");
          ReturnJson.put("MsgText", "已生成订单！");
          // return ReturnJson;
        }

        JSONObject first_result = alitrip_travel_trade_query_response
            .getJSONObject("alitrip_travel_trade_query_response").getJSONObject("first_result");

        // String
        // out_sku_id=first_result.getJSONObject("sub_orders").getJSONArray("sub_order_info").getJSONObject(0).getJSONObject("buy_item_info").getString("out_sku_id");
        String out_sku_id = alitrip_travel_trade_query_response.getString("out_sku_id");

        if (out_sku_id.equals("")) {
          System.out.println("out_sku_id=''");

          ReturnJson.put("MsgID", "-1");
          ReturnJson.put("MsgText", "未对照产品ID！");
          return ReturnJson;
        }
        ProductPackageMDao packageMDao = new ProductPackageMDao();
        ProductPackageMPO packageMPO = new ProductPackageMPO();

        packageMPO = packageMDao.Find(out_sku_id);
        if (packageMPO == null) {
          ReturnJson.put("MsgID", "-1");
          ReturnJson.put("MsgText", "无内部产品信息！");
          return ReturnJson;
        }

        JSONObject DataJson = new JSONObject();
        String ebo_id = "-1";

        try {
          ebo_id = SQLUilts.GetMaxID(SQLCon.GetConnect(), "5");
        } catch (SQLException e) {
          e.printStackTrace();
        }
        DataJson.put("ebo_id", ebo_id);
        DataJson.put("ebo_SourceName", "飞猪");
        DataJson.put("ebo_SourceOrderNo", ID);
        DataJson.put("ebo_StatusType", "未认领");
        DataJson.put("ebo_SaleName", "");

        DataJson.put("ebo_PackageName", packageMPO.get_Name());
        DataJson.put("Ebo_PackageCode", packageMPO.get_CodeOutSide());
        DataJson.put("Ebo_id_Epgm", "" + packageMPO.get_id());

//				System.out.println("购买人信息");
        // 购买人信息
        JSONObject buyer_info = first_result.getJSONObject("buyer_info");
        DataJson.put("ebo_SourceGuest", buyer_info.getString("buyer_nick"));
        DataJson.put("ebo_Remark", buyer_info.getString("buyer_message"));

        JSONObject sub_orders = first_result.getJSONObject("sub_orders");
        JSONArray sub_order_info = sub_orders.getJSONArray("sub_order_info");
        JSONObject contactor = sub_order_info.getJSONObject(0).getJSONObject("contactor");

        DataJson.put("ebo_LinkMan", contactor.getString("name"));
        DataJson.put("ebo_Phone", contactor.getString("phone"));
        DataJson.put("ebo_EMail", "");
        DataJson.put("ebo_WeiXin", "");
        DataJson.put("ebo_QQ", "");

        String post_address = contactor.getString("post_address");

        post_address = post_address.replaceAll("\\(|\\)", "");
        DataJson.put("ebo_Addr", post_address);
        // 付款信息

//				System.out.println("付款信息");
        JSONObject pay_info = first_result.getJSONObject("pay_info");
        DataJson.put("ebo_AccountDate", pay_info.getString("pay_time"));
        DataJson.put("ebo_PayDate", pay_info.getString("pay_time"));
        DataJson.put("ebo_PayMoney", df.format(sub_order_info.getJSONObject(0).getInt("payment") / 100));
        DataJson.put("ebo_AccountMoney", df.format(pay_info.getInt("received_payment") / 100));

        DataJson.put("ebo_PackageVisa", "");

        String PackageID = sub_order_info.getJSONObject(0).getString("sub_order_id_string");

        System.out.println("sub_order_id_string:" + PackageID);

        JSONObject PackageJson = GetPackage(ebo_id,out_sku_id, sub_order_info);
        System.out.println(PackageJson.toString());
        if (!PackageJson.getString("MsgID").equals("1")) {
          ReturnJson.put("MsgID", "-1");
          ReturnJson.put("MsgText", PackageJson.getString("MsgText"));
          return ReturnJson;
        }

        DataJson.put("ebo_id_Eva", Ebo_id_Eva );
        ReturnJson.put("OrderInfo", DataJson);

        ReturnJson.put("PackageInfo", PackageJson.getJSONArray("PackageInfo"));
        ReturnJson.put("NameList", PackageJson.getJSONArray("NameList"));

//				System.out.println(ReturnJson.toString());
        return ReturnJson;
      } else {
        ReturnJson.put("MsgID", "-1");
        ReturnJson.put("MsgText", "无对应订单记录！");
        return ReturnJson;
      }
    } finally {
      return ReturnJson;
    }
  }

  public JSONObject GetPackage(String eboID,String out_sku_id, JSONArray sub_order_info) {

    JSONObject ReturnArrayJson = new JSONObject();
    JSONArray PackageArrayJson = new JSONArray();
    JSONArray NameArrayJson = new JSONArray();

    DecimalFormat df = new DecimalFormat("0.00");
    
    Ebo_id_Eva=0;

//		System.out.println("GetPackage");

    JSONObject buy_item_info = sub_order_info.getJSONObject(0).getJSONObject("buy_item_info");

    JSONArray traveller_info = null;
    if (sub_order_info.getJSONObject(0).getJSONObject("travellers").has("traveller_info")) {
      traveller_info = sub_order_info.getJSONObject(0).getJSONObject("travellers").getJSONArray("traveller_info");
    }

    JSONObject contactor = sub_order_info.getJSONObject(0).getJSONObject("contactor");

//    String out_sku_id = buy_item_info.getString("out_sku_id");
    int Num = buy_item_info.getInt("num");
    float Order_discount_fee = sub_order_info.getJSONObject(0).getInt("discount_fee") / 100 / Num;
    float discount_fee = 0f;

    float price = buy_item_info.getInt("price") / 100f;

    ProductPackageDBODao packageDDao = new ProductPackageDBODao();
    ArrayList<ProductPackageDBO> packageDBOList = new ArrayList<ProductPackageDBO>();

    System.out.println("out_sku_id:"+out_sku_id);
    packageDBOList = packageDDao.Find(out_sku_id);

    for (int i = 0; i < Num; i++) {
      float ProductPrice = 0;
      for (int j = 0; j < packageDBOList.size(); j++) {
        ProductPrice = ProductPrice + packageDBOList.get(j).getEpgd_SaleMoney();
      }
//			System.out.println("price:"+price);
//			System.out.println("ProductPrice:"+ProductPrice);

      discount_fee = price - ProductPrice - Order_discount_fee;
      // -------------------------生成客人列表-------------------------
      JSONObject NameJson = new JSONObject();
      String Ebon_id = "-1";

      try {
        Ebon_id = SQLUilts.GetMaxID(SQLCon.GetConnect(), "5");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      BookingOrderNameListPO NameList = new BookingOrderNameListPO();
      NameList.set_id(Integer.parseInt(Ebon_id));
      NameList.set_status("I");
      NameList.set_id_Ebo(Integer.parseInt(eboID));
      NameList.set_StatusType("未处理");
      NameList.set_Name(contactor.getString("name"));

      NameList.set_id_avg(-1);
      NameList.set_id_ava(-1);
      NameList.set_User_Ins("接口");
      NameList.set_Date_Ins(new Date());

      if ((traveller_info != null) && (traveller_info.length() >= Num)) {
        NameList.set_PassPort(traveller_info.getJSONObject(i).getString("credential_no"));
        NameList.set_passPortNo(traveller_info.getJSONObject(i).getString("credential_no"));
        NameList.set_mobile(traveller_info.getJSONObject(i).getString("phone"));
        JSONObject extend_attributes_json = new JSONObject(
            traveller_info.getJSONObject(i).getString("extend_attributes_json"));
        String applyId = extend_attributes_json.getString("applyId");
        String surnamePinyin = extend_attributes_json.getString("surnamePinyin");
        String givenNamePinyin = extend_attributes_json.getString("givenNamePinyin");
        String currentApplyStatus = extend_attributes_json.getString("currentApplyStatus");
        NameList.set_ApplyId(applyId);
        if (!currentApplyStatus.equals("")) {
          NameList.set_StatusType(new TaoBaoOTAService().GetVisaStateName(Integer.parseInt(currentApplyStatus)));
        }
        if (!surnamePinyin.equals("")) {
          NameList.set_Name(surnamePinyin + " " + givenNamePinyin);
          NameList.set_firstname_c(givenNamePinyin);
          NameList.set_firstname_e(givenNamePinyin);
          NameList.set_lastname_c(surnamePinyin);
          NameList.set_lastname_e(surnamePinyin);
        }
      }
      NameJson = NameList.toJson("Ebon");
      NameArrayJson.put(NameJson);

      for (int j = 0; j < packageDBOList.size(); j++) {
        ProductPackageDBO bo = packageDBOList.get(j);

        JSONObject DataJson = new JSONObject();
        String ebop_id = "-1";
        try {
          ebop_id = SQLUilts.GetMaxID(SQLCon.GetConnect(), "5");
        } catch (SQLException e) {
          e.printStackTrace();
        }
        DataJson.put("ebop_id", ebop_id);
        DataJson.put("ebop_status", "I");
        DataJson.put("ebop_id_ebo", eboID);
        DataJson.put("ebop_id_epi", "" + bo.getEpi_id());
        DataJson.put("ebop_StatusType", "未完成");
        DataJson.put("ebop_ProductCode", bo.getEpi_code());
        DataJson.put("ebop_id_ept", bo.getEpi_Type());
        DataJson.put("ebop_id_act", bo.getEpi_id_act());
        DataJson.put("ebop_ProductName", bo.getEpgd_Name());
        DataJson.put("ebop_day", bo.getEpi_Day());
        DataJson.put("ebop_Money", (bo.getEpgd_SaleMoney() + discount_fee));
        DataJson.put("ebop_AddMoney", 0);
        DataJson.put("ebop_SaleMoney", (bo.getEpgd_SaleMoney() + discount_fee));
        DataJson.put("ebop_id_esi", bo.getEpi_id_Esi());
        DataJson.put("ebop_DateStart", "");
        DataJson.put("ebop_DateBack", "");
        DataJson.put("ebop_id_ebon", Ebon_id);
        PackageArrayJson.put(DataJson);
        discount_fee = 0;
        
        if (bo.getEpi_Type()==1 && bo.getEpi_id_Eva()!=0)
        {
          Ebo_id_Eva=bo.getEpi_id_Eva();
        }
      }
    }
    ReturnArrayJson.put("MsgID", "1");
    ReturnArrayJson.put("MsgText", "Success");
    ReturnArrayJson.put("PackageInfo", PackageArrayJson);
    ReturnArrayJson.put("NameList", NameArrayJson);

    return ReturnArrayJson;
  }

}
