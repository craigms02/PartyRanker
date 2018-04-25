
package ui;

import datalayer.LikeDao;
import datalayer.PartyDao;
import datalayer.UniqueIdDao;
import datalayer.UserDao;
import models.PartyModel;
import models.UserModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ViewPartiesServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * The post method is called by the browser when the user presses the button
     *
     * @param request The request has info on filled in fields and button presses.
     * @param response We use this to give the browser a response.
     * @throws ServletException
     * @throws IOException
     */

    String buttonValue;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);

        // View a party
        String viewButtonName = getButtonNameGivenValue(request, "View");
        if (viewButtonName != null) {
            int PartyId = Integer.parseInt(viewButtonName);
            handleViewButton(request, response, user, PartyId);
            return;
        }

        String partyText=request.getParameter("partyText");
        String buttonValue = request.getParameter("submitButton");



        // If submit was hit, add party.
        if (buttonValue != null && buttonValue.equals("Submit")){
            addParty(user,partyText);
        }
        // If like was hit, like the party
        String likeButtonName = getButtonNameGivenValue(request, "I'm going!");
        if (likeButtonName != null) {
            int partyId = Integer.parseInt(likeButtonName);
            likeParty(user, partyId);
        }

        // Delete a party
        String partyIdAsString = getButtonNameGivenValue(request, buttonValue = "Delete");
        if (partyIdAsString != null) {
            int PartyId = Integer.parseInt(partyIdAsString);
            PartyDao.deleteParty(PartyId);
        }

        // Load any data we need on the page into the request.
        request.setAttribute("user", user);
        loadPartiesIntoRequest(request);

        // Show the page
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewparties.jsp");
        dispatcher.forward(request, response);

    }

    private void likeParty(UserModel user, int partyId) {
        LikeDao.saveLike(partyId, user.getUsername());
    }

    private void handleViewButton(HttpServletRequest request, HttpServletResponse response, UserModel user, int partyId) throws ServletException, IOException {
        PartyModel party = PartyDao.getParty(partyId);

        request.getSession().setAttribute("partyid", partyId);

        //load any data we need into the request
        request.setAttribute("user", user);
        request.setAttribute("party", party);
        loadCommentsOnPartyIntoRequest(request, partyId);

        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewParty");
        dispatcher.forward(request, response);

    }


    /**
     * Grab the username from the request and create a user model.
     */
    private UserModel loadUserFromRequest(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        UserModel user = UserDao.getUser(username);

        // If there is no user for some weird reason, just use anonymous.
        if (user == null) {
            user = new UserModel();
            user.setUsername("anonymous");
        }
        request.setAttribute("user", user);
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
        // Before we go the page to display the stories, we need to get the stories.
        // And then shove the stories in to the request.
        loadUserFromRequest(request);
        loadPartiesIntoRequest(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewparties.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Retrieve all the stories and put them in the request.
     * We can then use then in the JSP file.
     *
     * @param request
     */
    private void loadPartiesIntoRequest(HttpServletRequest request) {
        ArrayList<PartyModel> storiesList = PartyDao.getParties();

        // We're going to convert the array list to an array because it works better in the JSP.
        PartyModel[] stories = storiesList.toArray(new PartyModel[storiesList.size()]);
        request.setAttribute("parties", stories);
    }
    private void loadCommentsOnPartyIntoRequest(HttpServletRequest request, int partyId) {
        ArrayList<PartyModel> storiesList = PartyDao.getPartiesThatAreComments(partyId);

        PartyModel[] stories = storiesList.toArray(new PartyModel[storiesList.size()]);
        request.setAttribute("partycomments", stories);
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
    private String getButtonNameGivenValue(HttpServletRequest request, String buttonValue) {
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue.equals(buttonValue)) {
                return paramName;
            }
        }
        return null;
    }

}
