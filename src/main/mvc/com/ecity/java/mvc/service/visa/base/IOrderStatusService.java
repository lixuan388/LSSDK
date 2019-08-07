package com.ecity.java.mvc.service.visa.base;

import com.ecity.java.json.JSONObject;

public interface IOrderStatusService {
  JSONObject UpdateMainName(String id, String Value);

  JSONObject UpdateERPName(String id, String Value);

  JSONObject UpdateState(String id, String Value);

  String GetState(String code);
}
