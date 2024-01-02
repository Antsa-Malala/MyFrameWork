<%@page import="java.util.ArrayList"%>
<%@page import="models.Plat"%>
<%@page import="models.Employe"%>
<%
    Object[] plat=(Object[])request.getAttribute("liste_plat");
    Object[] employe=(Object[])request.getAttribute("liste_employe");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Plat </title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
        <form action="insert_platC" method="POST" enctype="multipart/form-data">
            <h1>Entrez vos donnees</h1>
            <div>
                <label>Date : </label><br>
                <input type="date" name="daty">
            </div>
            <div>
                <label>Employe : </label><br>
                <select name="idemp">
                    <%for(int i=0;i<employe.length;i++){
                        Employe e=(Employe)employe[i];%>
                        <option value="<%out.print(e.getid());%>"><%out.print(e.getnom());%> <%out.print(e.getprenom());%></option>
                    <%}%>
                </select>
            </div>
            <div>
                <label>Plat : </label><br>
                <select name="idplat">
                    <%for(int i=0;i<plat.length;i++){
                        Plat p=(Plat)plat[i];%>
                        <option value="<%out.print(p.getid());%>"><%out.print(p.getlibelle());%></option>
                    <%}%>
                </select>
            </div>
            <div>
                <label>Photo : </label><br>
                <input type="file" name="sary">
            </div>
            <div>
                <input type="submit" value="OK">
            </div>
        </form> 
</body>
</html>