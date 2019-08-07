package com.ecity.java.mvc.model.visa.ota.system;

import com.ecity.java.mvc.model.ECityModel;

public class MessageTemplatePO extends ECityModel {
//TableName:Emt_MessageTemplate
//ColumnCount:8
  private int _id;
  private String _status;
  private String _User_Ins;
  private java.util.Date _Date_Ins;
  private String _User_Lst;
  private java.util.Date _Date_Lst;
  private String _Title;
  private String _Content;

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public String get_status() {
    return _status;
  }

  public void set_status(String _status) {
    this._status = _status;
  }

  public String get_User_Ins() {
    return _User_Ins;
  }

  public void set_User_Ins(String _User_Ins) {
    this._User_Ins = _User_Ins;
  }

  public java.util.Date get_Date_Ins() {
    return _Date_Ins;
  }

  public void set_Date_Ins(java.util.Date _Date_Ins) {
    this._Date_Ins = _Date_Ins;
  }

  public String get_User_Lst() {
    return _User_Lst;
  }

  public void set_User_Lst(String _User_Lst) {
    this._User_Lst = _User_Lst;
  }

  public java.util.Date get_Date_Lst() {
    return _Date_Lst;
  }

  public void set_Date_Lst(java.util.Date _Date_Lst) {
    this._Date_Lst = _Date_Lst;
  }

  public String get_Title() {
    return _Title;
  }

  public void set_Title(String _Title) {
    this._Title = _Title;
  }

  public String get_Content() {
    return _Content;
  }

  public void set_Content(String _Content) {
    this._Content = _Content;
  }

}
