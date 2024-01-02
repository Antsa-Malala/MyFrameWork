<%@page import="java.util.ArrayList"%>
<%@page import="models.PlatC"%>
<%@page import="models.Plat"%>
<%@page import="models.Employe"%>
<%
    Object[] platC=(Object[])request.getAttribute("liste_plat");
    ArrayList<String> listes=(ArrayList<String>)request.getAttribute("liste_sary");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Plat</title>
    <style>
        div{
            margin:20px;
        }
        img{
            width:100px;
        }
    </style>
</head>
<body>
         <h1>Liste des plats consommees </h1>
    <table class="table" id="tab">
            <thead>
                <tr>
                    <th scope="col">Date</th> 
                    <th scope="col">Employe</th> 
                    <th scope="col">Plat</th> 
                    <th scope="col">Sary</th> 
                    <th scope="col"></th> 
                </tr>
            </thead>
            <tbody>
                <%for(int i=0;i<platC.length;i++) { 
                PlatC t=(PlatC)platC[i];%>
                <tr>
                    <td scope="row"><%out.print(t.getdaty());%> </td>
                    <%Employe c=(Employe)new Employe(t.getidemp()).maka(null,"*")[0];%>
                    <td scope="row"><%out.print(c.getnom());%> <%out.print(c.getprenom());%></td>
                    <% Plat v=(Plat)new Plat(t.getidplat()).maka(null,"*")[0];%>
                    <td scope="row"><%out.print(v.getlibelle());%> </td>
                    <td><img src="data:image/jpeg;base64, <%= listes.get(i) %>" alt="Image"></td>
                    <td><a download="image.jpg" href="data:image/jpeg;base64, <%= listes.get(i) %>" title="Image">Telecharger</a></td>
                </tr>
                <% } %>
            </tbody>
        </table>
</body>
</html>