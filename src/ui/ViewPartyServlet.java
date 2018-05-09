
package ui;

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



public class ViewPartyServlet extends javax.servlet.http.HttpServlet {
    private Logger logger = Logger.getLogger(getClass().getName());


    String buttonValue;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logRequestParameters(request);  // Just to help with debugging.

        // Get data from the request
        UserModel user = loadUserFromRequest(request);

        // Delete a party
        String viewButtonName = getButtonNameGivenValue(request, buttonValue = "View");
        if (viewButtonName != null) {
            int PartyId = Integer.parseInt(viewButtonName);
            handleViewButton(request, response, user, PartyId);
        }

        String partyText=request.getParameter("partyText");
        String buttonValue = request.getParameter("submitButton");


        // If submit was hit, add a comment.
       // if (buttonValue != null && buttonValue.equals("Submit")){
       //     addComment(user, partyText, PartyId);
       // }
        String partyIdAsString = getButtonNameGivenValue(request, buttonValue = "Submit");
        if (partyIdAsString != null) {
            int PartyId = Integer.parseInt(partyIdAsString);
            addComment(user, partyText, PartyId);
        }

        // Delete a party
        partyIdAsString = getButtonNameGivenValue(request, buttonValue = "Delete");
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

//    Integer partyIdAsInteger = (Integer) request.getSession().getAttribute("partyid");
//
//    int partyBeingDisplayedId = 0;
//    if (partyIdAsInteger != null){
//        partyBeingDisplayedId = partyIdAsInteger.intValue();
    //}
    private void handleViewButton(HttpServletRequest request, HttpServletResponse response, UserModel user, int partyId) throws ServletException, IOException {
        PartyModel party = PartyDao.getParty(partyId);

        //load any data we need into the request
        request.setAttribute("user", user);
        request.setAttribute("party", party);
        RequestDispatcher dispatcher=request.getRequestDispatcher("/viewparty.jsp");
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
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before we go the page to display the stories, we need to get the stories.
        // And then shove the stories in to the request.
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

    /**
     * Save a story.
     */
    private void addComment(UserModel user, String partyText, int commentOnPartyId) {
        if (partyText != null && partyText.length() > 0 && user != null) {
            PartyDao.saveParty(UniqueIdDao.getID(), partyText, user.getUsername(), commentOnPartyId,"","");
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
