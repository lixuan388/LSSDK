package com.ecity.java.mvc.model.visa.ota;

import com.ecity.java.mvc.model.ECityModel;

public class ProductPackageDBO extends ECityModel {
//Sql:select Epgd_Name,Epgd_SaleMoney,Epgd_CostMoney,Epi_id,Epi_code,Epi_Type,Epi_id_act,Epi_Day,Epi_id_Esi
//from Epgm_Product_Package_M,Epgd_Product_Package_d ,Epi_Product_Info
//where Epgm_id=Epgd_id_Epgm and Epgd_id_Epi=Epi_id
//and Epgm_status<>'D' and Epgd_status<>'D' and Epi_status<>'D'
//ColumnCount:9
  private String epgd_Name;
  private float epgd_SaleMoney;
  private float epgd_CostMoney;
  private int epi_id;
  private String epi_code;
  private int epi_Type;
  private int epi_id_act;
  private int epi_Day;
  private int epi_id_Esi;
  private int epi_id_Eva;

  public String getEpgd_Name() {
    return epgd_Name;
  }

  public void setEpgd_Name(String epgd_Name) {
    this.epgd_Name = epgd_Name;
  }

  public float getEpgd_SaleMoney() {
    return epgd_SaleMoney;
  }

  public void setEpgd_SaleMoney(float epgd_SaleMoney) {
    this.epgd_SaleMoney = epgd_SaleMoney;
  }

  public float getEpgd_CostMoney() {
    return epgd_CostMoney;
  }

  public void setEpgd_CostMoney(float epgd_CostMoney) {
    this.epgd_CostMoney = epgd_CostMoney;
  }

  public int getEpi_id() {
    return epi_id;
  }

  public void setEpi_id(int epi_id) {
    this.epi_id = epi_id;
  }

  public String getEpi_code() {
    return epi_code;
  }

  public void setEpi_code(String epi_code) {
    this.epi_code = epi_code;
  }

  public int getEpi_Type() {
    return epi_Type;
  }

  public void setEpi_Type(int epi_Type) {
    this.epi_Type = epi_Type;
  }

  public int getEpi_id_act() {
    return epi_id_act;
  }

  public void setEpi_id_act(int epi_id_act) {
    this.epi_id_act = epi_id_act;
  }

  public int getEpi_Day() {
    return epi_Day;
  }

  public void setEpi_Day(int epi_Day) {
    this.epi_Day = epi_Day;
  }

  public int getEpi_id_Esi() {
    return epi_id_Esi;
  }

  public void setEpi_id_Esi(int epi_id_Esi) {
    this.epi_id_Esi = epi_id_Esi;
  }

  public int getEpi_id_Eva() {
    return epi_id_Eva;
  }

  public void setEpi_id_Eva(int epi_id_Eva) {
    this.epi_id_Eva = epi_id_Eva;
  }

}
