cd C:\Users\Antsa\Desktop\MyFrameWork\Framework
javac -d .\WEB-INF\classes *.java
cd .\WEB-INF\classes
jar cvf classes.jar *
copy classes.jar C:\Users\Antsa\Desktop\MyFrameWork\TestFramework\WEB-INF\lib
pause
