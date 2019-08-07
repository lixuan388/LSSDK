package com.ecity.java.mvc.service.system;

import java.util.Date;

import com.ecity.java.json.JSONArray;

public interface ISystemService {

  String GetProductTypeName(String id);

  String GetCountryName(String id);

  String GetSupplierName(String id);

  String[] GetBookingOrderStatusName();

  String GetVisaReturnDate(String avsID, String actID, String SendDate);

  String GetVisaNextWorkDate(String avsID, String actID, String SendDate);

  JSONArray GetBookingOrderStatusNameJson();

}
