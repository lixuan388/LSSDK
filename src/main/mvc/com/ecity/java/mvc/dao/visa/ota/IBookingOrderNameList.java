package com.ecity.java.mvc.dao.visa.ota;

import java.util.ArrayList;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.model.visa.ota.BookingOrderNameListPO;

public interface IBookingOrderNameList {
  BookingOrderNameListPO find(String id);

  ArrayList<BookingOrderNameListPO> findByEboID(String id);

  ArrayList<BookingOrderNameListPO> findByOrderNo(String OrderNo);

  JSONObject PostData(JSONObject data);

}
