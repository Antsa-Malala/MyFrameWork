<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire Employe</title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
        <form action="Emp/formulaire" method="POST">
            <h1>Entrez vos coordonnees en tant que employe </h1>
            <div>
                <label>Nom : </label><br>
                <input type="text" name="nom" placeholder="Votre nom">
            </div>
            <div>
                <label>Prenom : </label><br>
                <input type="text" name="prenom" placeholder="Votre prenom">
            </div>
            <div>
                <input type="submit" value="Envoyer">
            </div>
        </form> 
</body>
</html>