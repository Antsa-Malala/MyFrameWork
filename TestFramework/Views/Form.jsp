<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire</title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
        <form action="formulaire" method="POST">
            <h1>Entrez vos coordonnees </h1>
            <div>
                <label>Nom : </label><br>
                <input type="text" name="nom" placeholder="Votre nom">
            </div>
            <div>
                <label>Prenom : </label><br>
                <input type="text" name="prenom" placeholder="Votre prenom">
            </div>
            <div>
                <label>Age : </label><br>
                <input type="number" name="age" placeholder="Votre age" min="1">
            </div>
            <div>
                <label>Mesure : </label><br>
                <input type="text" name="mesure" placeholder="Votre mesure">
            </div>
            <div>
                <input type="submit" value="Envoyer">
            </div>
        </form> 
</body>
</html>