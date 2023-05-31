set chemin=C:\Users\Antsa\Desktop\MyFrameWork
mkdir %chemin%\temp
mkdir %chemin%\temp\WEB-INF
mkdir %chemin%\temp\WEB-INF\lib
mkdir %chemin%\temp\WEB-INF\classes
copy %chemin%\TestFramework\WEB-INF\lib\classes.jar %chemin%\temp\WEB-INF\lib\classes.jar
copy %chemin%\TestFramework\WEB-INF\web.xml %chemin%\temp\WEB-INF\web.xml 
mkdir %chemin%\temp\Views
copy %chemin%\TestFramework\Views\* %chemin%\temp\Views
javac -parameters -d %chemin%\temp\WEB-INF\classes  -classpath %chemin%\TestFramework\WEB-INF\lib\classes.jar %chemin%\TestFramework\*.java
cd %chemin%\temp
jar -cvf FrontServlet.war *
copy FrontServlet.war "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
cd %chemin%
rmdir temp /S/Q
pause