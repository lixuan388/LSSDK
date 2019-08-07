package com.ecity.java.mvc.dao.visa.base;

import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.base.OrderStatusPO;

public interface IOrderStatusDao {
  ArrayList<OrderStatusPO> Find1();

  ArrayList<OrderStatusPO> Find2();
}
