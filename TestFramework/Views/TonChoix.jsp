<%@page import="java.util.ArrayList"%>
<%@page import="models.Personne"%>
<%
    ArrayList<Personne> listes=(ArrayList<Personne>)request.getAttribute("Liste_personne");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Votre choix</title>
</head>
<body>
    <%for(int i=0;i<listes.size();i++) { %>
        <h1>Vous avez choisi : </h1>
        <%for(int j=0;j<listes.get(i).getoption().length;j++) { %>
            <p><%out.print(listes.get(i).getoption()[j]);%></p>
        <% } %>
    <% } %>
</body>
</html>