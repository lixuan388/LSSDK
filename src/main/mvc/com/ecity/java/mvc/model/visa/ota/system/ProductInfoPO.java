package com.ecity.java.mvc.model.visa.ota.system;

import com.ecity.java.mvc.model.ECityModel;

public class ProductInfoPO extends ECityModel {
//TableName:Epi_Product_Info
//ColumnCount:17
  private int _id;
  private String _status;
  private String _User_Ins;
  private java.util.Date _Date_Ins;
  private String _User_Lst;
  private java.util.Date _Date_Lst;
  private String _Code;
  private String _CodeOutSide;
  private String _Name;
  private int _id_Esi;
  private int _id_act;
  private int _Day;
  private int _Type;
  private int _InSideID;
  private String _InSideName;
  private float _CostMoney;
  private float _SaleMoney;

  private int _id_Eva;
  private int _id_Evt;

  public int get_id_Eva() {
    return _id_Eva;
  }

  public void set_id_Eva(int _id_Eva) {
    this._id_Eva = _id_Eva;
  }

  public int get_id_Evt() {
    return _id_Evt;
  }

  public void set_id_Evt(int _id_Evt) {
    this._id_Evt = _id_Evt;
  }

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

  public int get_id_Esi() {
    return _id_Esi;
  }

  public void set_id_Esi(int _id_Esi) {
    this._id_Esi = _id_Esi;
  }

  public int get_id_act() {
    return _id_act;
  }

  public void set_id_act(int _id_act) {
    this._id_act = _id_act;
  }

  public int get_Day() {
    return _Day;
  }

  public void set_Day(int _Day) {
    this._Day = _Day;
  }

  public int get_Type() {
    return _Type;
  }

  public void set_Type(int _Type) {
    this._Type = _Type;
  }

  public int get_InSideID() {
    return _InSideID;
  }

  public void set_InSideID(int _InSideID) {
    this._InSideID = _InSideID;
  }

  public String get_InSideName() {
    return _InSideName;
  }

  public void set_InSideName(String _InSideName) {
    this._InSideName = _InSideName;
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
