package com.ecity.java.mvc.model.visa.ota;

import com.ecity.java.mvc.model.ECityModel;

public class ProductPackageMPO extends ECityModel {
//TableName:Epgm_Product_Package_M
//ColumnCount:13
  private int _id;
  private String _status;
  private String _User_Ins;
  private java.util.Date _Date_Ins;
  private String _User_Lst;
  private java.util.Date _Date_Lst;
  private String _Code;
  private String _CodeOutSide;
  private String _Name;
  private String _Remark;
  private int _ManNum;
  private float _CostMoney;
  private float _SaleMoney;

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

  public String get_Code() {
    return _Code;
  }

  public void set_Code(String _Code) {
    this._Code = _Code;
  }

  public String get_CodeOutSide() {
    return _CodeOutSide;
  }

  public void set_CodeOutSide(String _CodeOutSide) {
    this._CodeOutSide = _CodeOutSide;
  }

  public String get_Name() {
    return _Name;
  }

  public void set_Name(String _Name) {
    this._Name = _Name;
  }

  public String get_Remark() {
    return _Remark;
  }

  public void set_Remark(String _Remark) {
    this._Remark = _Remark;
  }

  public int get_ManNum() {
    return _ManNum;
  }

  public void set_ManNum(int _ManNum) {
    this._ManNum = _ManNum;
  }

  public float get_CostMoney() {
    return _CostMoney;
  }

  public void set_CostMoney(float _CostMoney) {
    this._CostMoney = _CostMoney;
  }

  public float get_SaleMoney() {
    return _SaleMoney;
  }

  public void set_SaleMoney(float _SaleMoney) {
    this._SaleMoney = _SaleMoney;
  }

}
