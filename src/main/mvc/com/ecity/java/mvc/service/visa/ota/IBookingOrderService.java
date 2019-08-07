package com.ecity.java.mvc.service.visa.ota;

import java.util.ArrayList;

import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.model.visa.ota.BookingOrderHistoryPO;

public interface IBookingOrderService {

  JSONObject UpdateSaleName(String OrderID, String UserName);

  JSONObject InsertHistory(String eboID, String UserName, String Type, String Remark);

  ArrayList<BookingOrderHistoryPO> GetHistoryList(String eboID);

  JSONObject UpdateStatus(String eboID, String UserName, String Status, String whereStr);

  float getCountryVisaSpeedMoney(int avsID, int asiID);

  String GetMainOrderStatus(String status);

  JSONObject UpdateStatusByCode(String SourceOrderNo, String UserName, String Status, String whereStr);

  int GetOrderIDByCode(String SourceOrderNo);

  JSONObject UpdatePostaddress(String OrderID, String UserName);

  JSONObject GetPostAddress(String SourceOrderNo);

  JSONObject OrderSendGoods(JSONObject JsonData);
}
