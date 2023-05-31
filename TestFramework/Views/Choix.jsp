<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Choix</title>
    <style>
        div{
            margin:20px;
        }
    </style>
</head>
<body>
    <h1>Choisissez une option</h1>
    <form action="choice" method="POST">
        <div>
            <input type="checkbox" name="option" id="choix1" value="Dev">
            <label for="choix1">DEV</label>
        </div>
        <div>
            <input type="checkbox" name="option" id="choix2" value="Reseau">
            <label for="choix2">RESEAU</label>
        </div>
        <div>
            <input type="checkbox" name="option" id="choix3" value="web">
            <label for="choix3">WEB</label>
        </div>
        <div>
            <input type="submit" value="Valider">
        </div>
    </form> 
</body>
</html>