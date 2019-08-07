package com.ecity.java.mvc.dao.visa.ota;

import com.ecity.java.mvc.model.visa.ota.BookingOrderPO;

public interface IBookingOrder {

  BookingOrderPO find(String id);

  BookingOrderPO findByCode(String code);
}
