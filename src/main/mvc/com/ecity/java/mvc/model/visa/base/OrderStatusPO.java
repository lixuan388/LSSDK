package com.ecity.java.mvc.model.visa.base;

import com.ecity.java.mvc.model.ECityModel;

public class OrderStatusPO extends ECityModel {
//TableName:eos_OrderStatus
//ColumnCount:8
  private int _id;
  private int _type;
  private String _Code;
  private String _Name;
  private int _index;
  private String _MainName;
  private String _status;
  private String _ERPName;
  private String _state;

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public int get_type() {
    return _type;
  }

  public void set_type(int _type) {
    this._type = _type;
  }

  public String get_Code() {
    return _Code;
  }

  public void set_Code(String _Code) {
    this._Code = _Code;
  }

  public String get_Name() {
    return _Name;
  }

  public void set_Name(String _Name) {
    this._Name = _Name;
  }

  public int get_index() {
    return _index;
  }

  public void set_index(int _index) {
    this._index = _index;
  }

  public String get_MainName() {
    return _MainName;
  }

  public void set_MainName(String _MainName) {
    this._MainName = _MainName;
  }

  public String get_status() {
    return _status;
  }

  public void set_status(String _status) {
    this._status = _status;
  }

  public String get_ERPName() {
    return _ERPName;
  }

  public void set_ERPName(String _ERPName) {
    this._ERPName = _ERPName;
  }

  public String get_state() {
    return _state;
  }

  public void set_state(String _state) {
    this._state = _state;
  }

}
