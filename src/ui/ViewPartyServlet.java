package ui;

import datalayer.PartyDao;
import datalayer.UniqueIdDao;
import datalayer.UserDao;
import models.PartyModel;
import models.UserModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewPartyServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * The post method is called by the browser when the user presses the button
     *
     * @param request The request has info on filled in fields and button presses.
     * @param response We use this to give the browser a response.
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);
        String partyText=request.getParameter("partyText");
        String buttonValue = request.getParameter("submitButton");

        // If submit was hit, add a party.
        if (buttonValue != null && buttonValue.equals("Submit")){
            addParty(user, partyText);
        }

        // Load any data we need on the page into the request.
        request.setAttribute("user", user);
        loadPartiesIntoRequest(request);

        // Show the page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewparties.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * Grab the username from the request and create a user model.
     */
    private UserModel loadUserFromRequest(HttpServletRequest request) {
        String username=request.getParameter("username");
        UserModel user = UserDao.getUser(username);

        // If there is no user for some weird reason, just use anonymous.
        if (user == null) {
            user = new UserModel();
            user.setUsername("anonymous");
        }

        return user;
    }

    /**
     * The get method is called if the user directly invokes the URI.
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        // Before we go the page to display the parties, we need to get the parties.
        // And then shove the parties in to the request.
        loadPartiesIntoRequest(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewparties.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Retrieve all the parties and put them in the request.
     * We can then use then in the JSP file.
     *
     * @param request
     */
    private void loadPartiesIntoRequest(HttpServletRequest request) {
        ArrayList<PartyModel> partiesList = PartyDao.getParties();

        // We're going to convert the array list to an array because it works better in the JSP.
        PartyModel[] parties = partiesList.toArray(new PartyModel[partiesList.size()]);
        request.setAttribute("parties", parties);
    }

    /**
     * Save a party.
     */
    private void addParty(UserModel user, String partyText) {
        if (partyText != null && partyText.length() > 0 && user != null) {
            PartyDao.saveParty(UniqueIdDao.getID(), partyText, user.getUsername(), 0);
        }
    }

    /**
     * This method is useful in debugging what you got back in the
     * response from the user.
     *
     * @param request
     */
    private void logRequestParameters(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            logger.info("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
        }
    }

}
