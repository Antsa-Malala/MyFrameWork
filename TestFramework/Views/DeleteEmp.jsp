<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Suppression d'employe</title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
        <form action="Emp/delete" method="POST">
            <h1>Entrez les coordonnees de l'employe a supprimer</h1>
            <div>
                <label>Nom : </label><br>
                <input type="text" name="nom" placeholder="Nom">
            </div>
            <div>
                <label>Prenom : </label><br>
                <input type="text" name="prenom" placeholder="Prenom">
            </div>
            <div>
                <input type="submit" value="Supprimer">
            </div>
        </form> 
</body>
</html>