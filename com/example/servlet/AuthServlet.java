package com.example.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String token = authenticate(username, password);

        if (token != null) {
            request.getSession().setAttribute("authToken", token);
            response.sendRedirect("customers");
            response.getWriter().write("Authentication successful. Token: " + token);
        } else {
            response.getWriter().write("Authentication failed.");
        }
    }

    public String authenticate(String username, String password) {
        String token = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost("https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp");
            String json = String.format("{\"login_id\" : \"%s\", \"password\" :\"%s\"}", username, password);
            StringEntity entity = new StringEntity(json);
            postRequest.setEntity(entity);
            postRequest.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response Code: " + statusCode);

                if (statusCode == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    System.out.println("Full JSON Response: " + jsonResponse); // Log the JSON response

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode rootNode = mapper.readTree(jsonResponse);

                    // Check for access token
                    JsonNode tokenNode = rootNode.get("access_token");
                    if (tokenNode != null) {
                        token = tokenNode.asText();
                    } else {
                        System.out.println("Access token not found in the JSON response");
                    }
                } else {
                    System.out.println("Error during authentication: " + response.getStatusLine().getReasonPhrase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;
    }
}