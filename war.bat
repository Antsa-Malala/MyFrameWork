cd C:\Users\Antsa\Desktop\MyFrameWork\TestFramework
javac -d .\WEB-INF\classes  -classpath .\WEB-INF\lib\classes.jar *.java
jar -cvf FrontServlet.war *
copy FrontServlet.war "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
pause