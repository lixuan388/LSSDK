package com.ecity.java.mvc.service.taobao.ota;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.taobao.api.ApiException;

public interface ITaoBaoOTAService {

  JSONObject TravelTradeQuery(String OrderID, boolean IsUpdate) throws ApiException;

  JSONArray UpdateVisaApplicantInfo(String OrderNo) throws ApiException;

  JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String state) throws ApiException;

  JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String state, String PostNumber,
      String PostCompanyCode, String PostCompanyName, String FileName, String BookFileName, String BookTime,
      String BookPlace) throws ApiException;

}
