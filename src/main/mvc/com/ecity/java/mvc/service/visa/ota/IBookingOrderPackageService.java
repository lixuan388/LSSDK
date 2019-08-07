package com.ecity.java.mvc.service.visa.ota;

import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.ota.BookingOrderPackagePO;

public interface IBookingOrderPackageService {
  ArrayList<BookingOrderPackagePO> findByeboID(String eboID);

  String Name(String ebonID);

  String PassPort(String ebonID);

  String GetAvgID(String ebonID);
}
