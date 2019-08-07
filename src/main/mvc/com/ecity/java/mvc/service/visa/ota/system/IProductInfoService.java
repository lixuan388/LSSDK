package com.ecity.java.mvc.service.visa.ota.system;

import com.ecity.java.mvc.model.visa.ota.system.ProductInfoPO;

public interface IProductInfoService {
  ProductInfoPO find(String id);

  ProductInfoPO insert();

}
