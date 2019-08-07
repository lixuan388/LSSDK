package com.ecity.java.mvc.model.system;

import com.ecity.java.mvc.model.ECityModel;

public class OperationButtonPO extends ECityModel {
//TableName:sob_Operation_button
//ColumnCount:5
  private int _id;
  private String _status;
  private String _name;
  private String _ButtonName;
  private int _index;
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
  public String get_name() {
    return _name;
  }
  public void set_name(String _name) {
    this._name = _name;
  }
  public String get_ButtonName() {
    return _ButtonName;
  }
  public void set_ButtonName(String _ButtonName) {
    this._ButtonName = _ButtonName;
  }
  public int get_index() {
    return _index;
  }
  public void set_index(int _index) {
    this._index = _index;
  }

}
