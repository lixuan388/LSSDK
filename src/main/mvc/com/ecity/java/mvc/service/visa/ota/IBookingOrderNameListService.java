package com.ecity.java.mvc.service.visa.ota;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;

public interface IBookingOrderNameListService {

  JSONObject PostData(JSONArray data);

  boolean UpdateApplyID(String id, String ApplyID);

  boolean UpdateApplictionStateByApplyID(String applyId, String state);

  JSONObject UpdateApplyState(String OrderNo);

  boolean UpdateApplyIDByOrderID(String OrderID);

  boolean UpdateApplyIDByCertNo(String EboID,String cert_no, String ApplyID);

  JSONObject UpdateVisaApplicantInfo(String OrderNo,String OrderID);

  JSONObject TravelvisaApplicantUpdate(String OrderNo, String ApplyID, String alitripCode,String currentApplyStatus,String PostNumber,String PostCompanyCode,
      String PostCompanyName,String FileName,String BookFileName,String BookTime,String BookPlace);

}
