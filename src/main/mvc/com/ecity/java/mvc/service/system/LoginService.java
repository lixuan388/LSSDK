package com.ecity.java.mvc.service.system;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLCon;

public class LoginService {
  
  public void CreateSession(HttpServletRequest req,String UserID)
  {
    DBTable table=new DBTable(SQLCon.GetConnect(),"select AUs_ID,AUs_UserName,AUs_UserCode,isnull(aus_temp3,'')  as aus_password,aco_id,aco_chn_name,aus_depart \r\n"
        + "from aus_users,aco_company \r\n" + "where aus_status<>'D' \r\n" + "and aus_id_aco=aco_id \r\n"
        + "and aus_id=" + UserID );

    try {
      table.Open();
      if (table.next()) {
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        // 将数据存储到session�?
        session.setAttribute("UserSessionID", sessionId);
        session.setAttribute("UserID", table.getString("AUs_ID"));
        session.setAttribute("UserName", table.getString("AUs_UserName"));
        session.setAttribute("UserCode", table.getString("AUs_UserCode"));
        
        String Depart=table.getString("aus_depart");
        if (Depart.equals("电商部"))
        {
          session.setAttribute("DepartmentID", "3");
          session.setAttribute("DepartmentName", "广州电商");
        }
        else
        {
          session.setAttribute("DepartmentID", table.getString("aco_id"));
          session.setAttribute("DepartmentName", table.getString("aco_chn_name"));
        }
        session.setAttribute("Depart", table.getString("aus_depart"));

      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally {
      table.CloseAndFree();
      
    }
  }

}
