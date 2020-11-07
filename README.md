# baseapp
Baseapp test

How to start service :
1. run "baseapp/mbs-common/buildAll.bat"
2. run "baseapp/mbs-java/buildAll.bat"
3. open cmd to folder "/baseapp/mbs-java/master-rest/" 
   and then run "java -jar target/master-rest-1.0.0.jar --spring.config.additional-location=../mbs-conf/local.properties --server.port={port}"
   
Postman
https://documenter.getpostman.com/view/13002820/TVeiCqcT

swagger
http://localhost:{port}/swagger-ui.html