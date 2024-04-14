package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validators.IValidator;
import validators.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/controller-servlet")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("./area-reply-servlet").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if(new Validator(req).checkFormat()) {
            req.getRequestDispatcher("./area-check-servlet").forward(req, resp);
        } else{
            sendError(resp, "Data is incorrect");
        }
    }

    public static void sendError(HttpServletResponse resp, String message) throws IOException {
        Gson json = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>() {{
            put("error", "Data is incorrect");
            put("status", "UNPROCESSABLE_ENTITY");
        }};

        resp.setContentType("application/json");
        resp.getWriter().write(json.toJson(jsonResponse));
        resp.setStatus(422);
    }
}
