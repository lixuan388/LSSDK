package com.ecity.java.mvc.dao.visa.ota.system;

import java.util.ArrayList;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.model.visa.ota.system.MessageTemplatePO;

public interface IMessageTemplateDao {

  ArrayList<MessageTemplatePO> Find();

  MessageTemplatePO FindByID(String ID);

  JSONObject Post(JSONArray data);

  JSONObject DeleteDelete(String ID, String UserName);

}
