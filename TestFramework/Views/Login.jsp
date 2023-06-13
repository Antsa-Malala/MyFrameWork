<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire authentification</title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
    <form action="Login" method="POST">
        <h1>Entrez vos donnees d'authentification </h1>
        <div>
            <label>Nom d'utilisateur : </label><br>
            <input type="text" name="username" placeholder="Nom d'utilisateur">
        </div>
        <div>
            <label>Mot de passe : </label><br>
            <input type="text" name="mdp" placeholder="Mot de passe">
        </div>
        <div>
            <input type="submit" value="Envoyer">
        </div>
    </form> 
</body>
</html>