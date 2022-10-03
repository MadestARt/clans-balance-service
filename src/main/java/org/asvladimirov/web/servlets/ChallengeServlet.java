package org.asvladimirov.web.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import org.asvladimirov.service.ChallengeService;
import org.asvladimirov.service.impl.ChallengeServiceImpl;
import org.asvladimirov.web.api.SendGoldResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/api/v1/challenge",name = "ChallengeServlet")
public class ChallengeServlet extends HttpServlet {

    private final ChallengeService challengeService = ChallengeServiceImpl.getInstance();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userId = Long.parseLong(req.getParameter("userId"));
        var clanID = Long.parseLong(req.getParameter("clanId"));
        var challengeId = Long.parseLong(req.getParameter("challengeId"));
        var response = new Gson().toJson(new SendGoldResponse(challengeService.startChallenge(userId, challengeId, clanID)));
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(response);
        out.flush();
    }
}
