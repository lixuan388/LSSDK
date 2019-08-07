package com.ecity.java.mvc.model.visa.ota;

import java.util.Date;

import com.ecity.java.mvc.model.ECityModel;

/**
 * ota订单，客人名单
 */
public class BookingOrderNameListPO extends ECityModel {
//TableName:Ebon_BookingOrder_NameList
//ColumnCount:30
  private int _id;
  private String _status;
  private String _User_Ins;
  private Date _Date_Ins;
  private String _User_Lst;
  private Date _Date_Lst;
  private int _id_Ebo;
  private String _StatusType;
  private String _Name;
  private String _PassPort;
  private int _id_avg;
  private int _id_ava;
  private String _name_c;
  private String _name_e;
  private String _passPortNo;
  private int _sex;
  private Date _date_birth;
  private Date _date_Sign;
  private Date _date_End;
  private String _place_issue;
  private String _country_code;
  private int _age;
  private String _lastname_c;
  private String _lastname_e;
  private String _lastname_p;
  private String _firstname_c;
  private String _firstname_e;
  private String _firstname_p;
  private String _ApplyId;
  private String _mobile;  
  
  private String _StatusOTA;
  private String _StatusOTA2;
  private String _StatusVisa;
  private String _StatusVisa2;

  private String _currentApplyStatus;
  private String _nextApplyStatus;
  
  
  public String get_currentApplyStatus() {
    return _currentApplyStatus;
  }

  public void set_currentApplyStatus(String _currentApplyStatus) {
    this._currentApplyStatus = _currentApplyStatus;
  }

  public String get_nextApplyStatus() {
    return _nextApplyStatus;
  }

  public void set_nextApplyStatus(String _nextApplyStatus) {
    this._nextApplyStatus = _nextApplyStatus;
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

  public Date get_Date_Ins() {
    return _Date_Ins;
  }

  public void set_Date_Ins(Date _Date_Ins) {
    this._Date_Ins = _Date_Ins;
  }

  public String get_User_Lst() {
    return _User_Lst;
  }

  public void set_User_Lst(String _User_Lst) {
    this._User_Lst = _User_Lst;
  }

  public Date get_Date_Lst() {
    return _Date_Lst;
  }

  public void set_Date_Lst(Date _Date_Lst) {
    this._Date_Lst = _Date_Lst;
  }

  public int get_id_Ebo() {
    return _id_Ebo;
  }

  public void set_id_Ebo(int _id_Ebo) {
    this._id_Ebo = _id_Ebo;
  }

  public String get_StatusType() {
    return _StatusType;
  }

  public void set_StatusType(String _StatusType) {
    this._StatusType = _StatusType;
  }

  public String get_Name() {
    return _Name;
  }

  public void set_Name(String _Name) {
    this._Name = _Name;
  }

  public String get_PassPort() {
    return _PassPort;
  }

  public void set_PassPort(String _PassPort) {
    this._PassPort = _PassPort;
  }

  public int get_id_avg() {
    return _id_avg;
  }

  public void set_id_avg(int _id_avg) {
    this._id_avg = _id_avg;
  }

  public int get_id_ava() {
    return _id_ava;
  }

  public void set_id_ava(int _id_ava) {
    this._id_ava = _id_ava;
  }

  public String get_name_c() {
    return _name_c;
  }

  public void set_name_c(String _name_c) {
    this._name_c = _name_c;
  }

  public String get_name_e() {
    return _name_e;
  }

  public void set_name_e(String _name_e) {
    this._name_e = _name_e;
  }

  public String get_passPortNo() {
    return _passPortNo;
  }

  public void set_passPortNo(String _passPortNo) {
    this._passPortNo = _passPortNo;
  }

  public int get_sex() {
    return _sex;
  }

  public void set_sex(int _sex) {
    this._sex = _sex;
  }

  public Date get_date_birth() {
    return _date_birth;
  }

  public void set_date_birth(Date _date_birth) {
    this._date_birth = _date_birth;
  }

  public Date get_date_Sign() {
    return _date_Sign;
  }

  public void set_date_Sign(Date _date_Sign) {
    this._date_Sign = _date_Sign;
  }

  public Date get_date_End() {
    return _date_End;
  }

  public void set_date_End(Date _date_End) {
    this._date_End = _date_End;
  }

  public String get_place_issue() {
    return _place_issue;
  }

  public void set_place_issue(String _place_issue) {
    this._place_issue = _place_issue;
  }

  public String get_country_code() {
    return _country_code;
  }

  public void set_country_code(String _country_code) {
    this._country_code = _country_code;
  }

  public int get_age() {
    return _age;
  }

  public void set_age(int _age) {
    this._age = _age;
  }

  public String get_lastname_c() {
    return _lastname_c;
  }

  public void set_lastname_c(String _lastname_c) {
    this._lastname_c = _lastname_c;
  }

  public String get_lastname_e() {
    return _lastname_e;
  }

  public void set_lastname_e(String _lastname_e) {
    this._lastname_e = _lastname_e;
  }

  public String get_lastname_p() {
    return _lastname_p;
  }

  public void set_lastname_p(String _lastname_p) {
    this._lastname_p = _lastname_p;
  }

  public String get_firstname_c() {
    return _firstname_c;
  }

  public void set_firstname_c(String _firstname_c) {
    this._firstname_c = _firstname_c;
  }

  public String get_firstname_e() {
    return _firstname_e;
  }

  public void set_firstname_e(String _firstname_e) {
    this._firstname_e = _firstname_e;
  }

  public String get_firstname_p() {
    return _firstname_p;
  }

  public void set_firstname_p(String _firstname_p) {
    this._firstname_p = _firstname_p;
  }

  public String get_ApplyId() {
    return _ApplyId;
  }

  public void set_ApplyId(String _ApplyId) {
    this._ApplyId = _ApplyId;
  }

  public String get_mobile() {
    return _mobile;
  }

  public void set_mobile(String _mobile) {
    this._mobile = _mobile;
  }

  public String get_StatusOTA() {
    return _StatusOTA;
  }

  public void set_StatusOTA(String _StatusOTA) {
    this._StatusOTA = _StatusOTA;
  }

  public String get_StatusOTA2() {
    return _StatusOTA2;
  }

  public void set_StatusOTA2(String _StatusOTA2) {
    this._StatusOTA2 = _StatusOTA2;
  }

  public String get_StatusVisa() {
    return _StatusVisa;
  }

  public void set_StatusVisa(String _StatusVisa) {
    this._StatusVisa = _StatusVisa;
  }

  public String get_StatusVisa2() {
    return _StatusVisa2;
  }

  public void set_StatusVisa2(String _StatusVisa2) {
    this._StatusVisa2 = _StatusVisa2;
  }

}
