package org.asvladimirov.web.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import org.asvladimirov.exception.SQLTransactionException;
import org.asvladimirov.service.UserService;
import org.asvladimirov.service.impl.UserServiceImpl;
import org.asvladimirov.web.api.ErrorResponse;
import org.asvladimirov.web.api.SendGoldResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/api/v1/user",name = "UserServlet")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserServiceImpl.getInstance();


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userId = Long.parseLong(req.getParameter("userId"));
        var clanID = Long.parseLong(req.getParameter("clanId"));
        var amount = Integer.parseInt(req.getParameter("amount"));
        try {
            var sendingGoldResult = userService.sendGoldToClan(userId, clanID, amount);
            var response = new Gson().toJson(new SendGoldResponse(sendingGoldResult));
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(response);
            out.flush();
        } catch (SQLTransactionException e) {
            var response = new Gson().toJson(new ErrorResponse(e.getMessage()));
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(response);
            out.flush();
        }
    }
}
