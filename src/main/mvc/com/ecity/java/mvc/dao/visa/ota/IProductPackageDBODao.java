package com.ecity.java.mvc.dao.visa.ota;

import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.ota.ProductPackageDBO;

public interface IProductPackageDBODao {

  ArrayList<ProductPackageDBO> Find(String code);
}
