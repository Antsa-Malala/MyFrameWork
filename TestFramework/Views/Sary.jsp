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
        <h1>Vous avez importe : </h1>
        <p>Nom : <%out.print(listes.get(i).getsary().getname());%></p>
        <p>Path : <%out.print(listes.get(i).getsary().getpath());%></p>
        <p>Taille : <%out.print(listes.get(i).getsary().getsary().length);%></p>
    <% } %>
</body>
</html>