package com.ecity.java.mvc.model.SF;

import com.ecity.java.mvc.model.ECityModel;

public class WayBillOrderPO extends ECityModel {
//TableName:ewbo_WayBillOrder
//ColumnCount:7
  private int _id;
  private String _user_ins;
  private java.util.Date _date_ins;
  private String _OrderID;
  private String _SourceOrderNo;
  private String _Type;
  private String _Content;

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String get_user_ins() {
    return _user_ins;
  }

  public void set_user_ins(String _user_ins) {
    this._user_ins = _user_ins;
  }

  public java.util.Date get_date_ins() {
    return _date_ins;
  }

  public void set_date_ins(java.util.Date _date_ins) {
    this._date_ins = _date_ins;
  }

  public String get_OrderID() {
    return _OrderID;
  }

  public void set_OrderID(String _OrderID) {
    this._OrderID = _OrderID;
  }

  public String get_SourceOrderNo() {
    return _SourceOrderNo;
  }

  public void set_SourceOrderNo(String _SourceOrderNo) {
    this._SourceOrderNo = _SourceOrderNo;
  }

  public String get_Type() {
    return _Type;
  }

  public void set_Type(String _Type) {
    this._Type = _Type;
  }

  public String get_Content() {
    return _Content;
  }

  public void set_Content(String _Content) {
    this._Content = _Content;
  }

}
