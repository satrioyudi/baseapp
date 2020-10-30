call mvn -f %~dp0\common-bom\pom.xml install
call mvn -f %~dp0\stereotype\pom.xml clean install
call mvn -f %~dp0\common-util\pom.xml clean install
call mvn -f %~dp0\common-core\pom.xml clean install
call mvn -f %~dp0\common-exception\pom.xml clean install
call mvn -f %~dp0\rest-server\pom.xml clean install
pause