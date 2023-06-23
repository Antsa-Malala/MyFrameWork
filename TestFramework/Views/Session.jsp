<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
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
    <title>Employe</title>
</head>
<body>
    <%for(int i=0;i<listes.size();i++) {
        Set<String> keys = listes.get(i).getsession().keySet();
        for (String key : keys) {
            out.println(key+" : "+ listes.get(i).getsession().get(key));
        %>
        <hr>
       <% }
    } %>
</body>
</html>