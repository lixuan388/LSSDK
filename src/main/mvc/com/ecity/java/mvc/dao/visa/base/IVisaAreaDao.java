package com.ecity.java.mvc.dao.visa.base;

import java.util.ArrayList;

import com.ecity.java.mvc.model.visa.base.VisaAreaPO;

public interface IVisaAreaDao {

  ArrayList<VisaAreaPO> Find();

  VisaAreaPO FindByID(String ID);

}
