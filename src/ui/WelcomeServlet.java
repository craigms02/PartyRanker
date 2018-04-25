package ui;

import datalayer.UserDao;
import models.UserModel;

import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.logging.Logger;

public class WelcomeServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        UserModel user = null;

        // Load data from the request
        String buttonValue = request.getParameter("button");
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        // Create an account
        if (buttonValue != null && buttonValue.equals("Create Account") && username != null){
            user = new UserModel();
            user.setUsername(username);
            user.setPassword(password);
            UserDao.saveUser(user);
        }

        // Or log in
        else if (buttonValue != null && buttonValue.equals("Log In")){
            user = UserDao.getUser(username);
            if (user == null || !user.getPassword().equals(password)){
                // We don't know who this is.
                // We're going to stay on this page.
                RequestDispatcher dispatcher=request.getRequestDispatcher("/welcome.jsp");
                dispatcher.forward(request, response);
                return;
            }
            request.getSession().setAttribute("username",username);
        }

        // Or by anonymous
        else if (buttonValue != null && buttonValue.equals("Be Anonymous")){
            user = new UserModel();
            user.setUsername("anonymous");
            UserDao.saveUser(user);
        }
        else {
            user = new UserModel();
            user.setUsername("anonymous");
            UserDao.saveUser(user);
        }
        //This is a legit user
        request.getSession().setAttribute("username", user.getUsername());

        // Load any data we need on the page into the request.
        request.setAttribute("user", user);
        request.getSession().setAttribute("username", user.getUsername());
        request.setAttribute("password", user.getPassword());
        request.getSession().setAttribute("password", user.getPassword());
        // Show the stories page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewParties");
        dispatcher.forward(request, response);
    }


     //The get method is invoked when the user goes to the page by browser URI.
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
        dispatcher.forward(request, response);
    }

}
