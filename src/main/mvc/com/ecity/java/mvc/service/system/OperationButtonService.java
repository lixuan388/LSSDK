package com.ecity.java.mvc.service.system;

import java.util.ArrayList;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.mvc.dao.system.OperationButtonDao;
import com.ecity.java.mvc.model.system.OperationButtonPO;

public class OperationButtonService {
  
  public JSONArray find(String ID)
  {
    JSONArray JsonList =new JSONArray();
    
    OperationButtonDao dao=new OperationButtonDao();
    
    ArrayList<OperationButtonPO> DataList=dao.find(ID);
    
    for (int i =0;i<DataList.size();i++)
    {
      OperationButtonPO po=DataList.get(i);
      JSONObject json=po.toJson();
      JsonList.put(json);
    }
    
    return JsonList;
    
  }
  

}
