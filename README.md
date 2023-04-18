# MyFrameWork
* Chaque fonction définie dans une classe doit être annotée
    avec @Model(url="url_pour_acceder_a_ma_fonction")

* Pour renvoyer des données, instancer la classe ModelView exemple:
    - vue: exemple.setview("Votre vue.jsp")
    - donnee : exemple.addItem("Nom_attribut",valeur_a_ajouter)

* Toutes les classes .java sont placées à la racine de l'application et contenus dans un seul package 

* Le contenu de votre fichier "web.xml" devrait contenir la servlet suivante
    <servlet>
        <servlet-name>MyController</servlet-name>
        <servlet-class>etu1963.framework.servlet.FrontServlet</servlet-class>
        <async-supported>true</async-supported>
        <init-param>
            <param-name>Package</param-name>
            <param-value>package_de_vos_classejava.class</param-value>
            <description>Nom du package de toutes les modeles</description>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>MyController</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    
* Si il y a des insertion à travers des formulaires le rang des champs dans le formulaire devrait être le même que le rang des arguments de la fonction qui va les traiter