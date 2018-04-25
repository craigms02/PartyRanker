<%@ page import="models.PartyModel" %>
<%@ page import="models.UserModel" %>
<%@ page import="datalayer.LikeDao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<title>Future Parties</title>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="resources/style.css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
      integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css"
      integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
        crossorigin="anonymous"></script>

</head>
<body>
<!-- Let's start by loading information we expect in the request.
     For any info missing, we'll just fake it.
  -->
<%
    UserModel user = (UserModel) request.getAttribute("user");
    if (user == null) {
        user = new UserModel();
        user.setUsername("anonymous");
    }

    PartyModel parties[] = (PartyModel[]) request.getAttribute("parties");
    if (parties == null) {
        parties = new PartyModel[0];
    }
    int numberComments = 0;
    for (int i = parties.length - 1; i >= 0; i--) {
        if (parties[i].getCommentOnPartyID() != 0)
            numberComments++;
    }
%>
<p></p>
<p></p>
<div class="container">

    <form action="viewParties" method="post">

        <!-- Navigation Bar -->
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li class="active"><a href="viewParties">Future Parties</a></li>
                        <li class="inactive"><a href="viewpartiespassed">Past Parties</a></li>
                        <li class="inactive"><a href=""><%=user.getUsername()%>'s Party Info</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="welcome"><span class="glyphicon glyphicon-log-out"></span>Exit</a></li>
                    </ul>
                </div>
            </div>
        </nav>


        <!-- Display a list of parties -->
        <div class="container">
            <div class="row">
                <div class="well well-sm">
                    <h3><p class="text-primary"><%=parties.length - numberComments%> Parties</h3>
                    <div class="pre-scrollable">
                        <ul class="list-group">
                            <%
                                for (int i = parties.length - 1; i >= 0; i--) {
                                    if (parties[i].getCommentOnPartyID() != 0)
                                        continue;


                            %>
                            <li class="list-group-item">[<%=parties[i].getUsername()%>] - <%=parties[i].getParty()%>
                                <input type="submit" class="btn btn-info" name="<%=parties[i].getPartyId()%>" value="Delete">
                                <input type="submit" class="btn btn-info" name="<%=parties[i].getPartyId()%>" value="View">
                                <input type="submit" class="btn btn-info" name="<%=parties[i].getPartyId()%>" value="I'm going!">
                                Expected Attendance: <%=LikeDao.getNumberOfLikes(parties[i].getPartyId())%>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Input for a new party -->
        <div class="container">
            <div class="row">
                <div class="well well-sm">
                <div class="form-group">
                    <label for="partyText">Details about your party</label>
                    <div class="form-group">
                        <input type="text" class="form-control" id="partyText" name="partyText"
                               placeholder="Ex. Woodside 6: 05/10/18">
                    </div>
                    <!-- Button -->
                    <input type="submit" class="btn btn-info" name="submitButton" value="Submit">
                </div>
                </div>
            </div>
        </div>


        <!-- This is a screet input to the post!  Acts as if the user
             had an input field with the username.
         -->
        <input type="hidden" name="username" value="<%=user.getUsername()%>">

    </form>
</div>
</body>
</html>
