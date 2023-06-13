<%@page import="java.util.ArrayList"%>
<%@page import="models.Emp"%>
<%
    ArrayList<Emp> listes=(ArrayList<Emp>)request.getAttribute("Liste_emp");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Supprime</title>
</head>
<body>
    <%for(int i=0;i<listes.size();i++) { %>
        <h1>L'employe <%out.print(listes.get(i).getprenom()+" "+listes.get(i).getnom());%> a ete supprime avec succes</h1>
    <% } %>
</body>
</html>