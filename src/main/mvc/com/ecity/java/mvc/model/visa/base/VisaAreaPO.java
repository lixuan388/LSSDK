package com.ecity.java.mvc.model.visa.base;

import com.ecity.java.mvc.model.ECityModel;

public class VisaAreaPO extends ECityModel {
//TableName:Eva_View_Area
//ColumnCount:5
  private int _id;
  private String _status;
  private String _Name;
  private String _Code;
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

  public String get_Name() {
    return _Name;
  }

  public void set_Name(String _Name) {
    this._Name = _Name;
  }

  public String get_Code() {
    return _Code;
  }

  public void set_Code(String _Code) {
    this._Code = _Code;
  }

  public int get_index() {
    return _index;
  }

  public void set_index(int _index) {
    this._index = _index;
  }

}
