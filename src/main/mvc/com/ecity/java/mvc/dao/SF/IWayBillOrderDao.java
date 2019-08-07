package com.ecity.java.mvc.dao.SF;

import java.util.ArrayList;

import com.ecity.java.mvc.model.SF.WayBillOrderPO;

public interface IWayBillOrderDao {

  boolean insert(String _user_ins, String _SourceOrderNo, String _OrderID, String _Type, String _Content);

  ArrayList<WayBillOrderPO> FindByCode(String Code);
}
