# MyFrameWork
* Votre projet doit être contenu dans un dossier nommé FrontServlet

* Chaque url saisi lors de l'execution de programme doit donc débuter par FrontServlet/votre url

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

* Toutes les classes .java sont placées à la racine de l'application et contenus dans un seul package 

* Chaque fonction définie dans une classe doit 
    - être annotée avec @Model(url="url_pour_acceder_a_ma_fonction")
    - retourner un objet de type ModelView

* Vos setters et getters devront être:
    setnom_attribut
    getnom_attribut

* Pour renvoyer des données, instancer la classe ModelView exemple:
    ModelView exemple=new ModelView()
    - vue: exemple.setview("Votre vue.jsp")
    - donnee : exemple.addItem("Nom_attribut",valeur_a_ajouter(de type objet))

* Votre projet devrait être compiler avec les options
    -parameters
    -classpath [le_chemin_ou_se_trouve_le_jar]

* Vos vues devront être placées dans un dossier nommer Views à la racine de l'application
    