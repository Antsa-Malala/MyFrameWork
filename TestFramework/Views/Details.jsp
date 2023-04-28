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
    <title>Details</title>
</head>
<body>
    <h1>Details de la personne</h1>
    <table class="table" id="tab">
            <thead>
                <tr>
                    <th scope="col">Nom</th>
                    <th scope="col">Prenom</th>
                    <th scope="col">Age</th>
                    <th scope="col">Mesure</th>  
                </tr>
            </thead>
            <tbody>
                <%for(int i=0;i<listes.size();i++) { %>
                <tr>
                    <td scope="row"><%out.print(listes.get(i).getnom());%> </td>
                    <td scope="row"><%out.print(listes.get(i).getprenom());%> </td>
                    <td scope="row"><%out.print(listes.get(i).getage());%></td>
                    <td scope="row"><%out.print(listes.get(i).getmesure());%></td>
                </tr>
                <% } %>
            </tbody>
        </table>
</body>
</html>