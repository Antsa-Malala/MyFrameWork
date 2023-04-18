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
    <title>Bienvenue</title>
</head>
<body>
    <%for(int i=0;i<listes.size();i++) { %>
        <h1>Bienvenue <%out.print(listes.get(i).getprenom()+" "+listes.get(i).getnom()+". Vous avez "+listes.get(i).getage()+"an(s) et vous mesurez "+listes.get(i).getmesure()+" metres");%> </h1>
    <% } %>
</body>
</html>