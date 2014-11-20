#!/bin/sh  
#memcached auto-start   
#  
# description: Auto-starts memcached  
# processname: memcached  
# pidfile: /var/memcached.pid   

 
case $1 in  
p)  
    mvn clean package -Pproduction -Dmaven.test.skip
    ;;
pt)
	mvn clean package -Ptest -Dmaven.test.skip
	;;
ci)
	mvn clean install -Dmaven.test.skip -DdownloadSources=true
	;;
ee)
	mvn eclipse:eclipse -Dwtpversion=2.0 -DdownloadSources=true
	;;
ec)
	mvn eclipse:clean
	;;
td)
	mvnDebug tomcat:run
	;;
t)
	mvn tomcat:run
	;;
s)
	mvn dependency:sources
	;;
gb)
	git branch -av --color
	;;
*)  
    echo 'p:package -Pproduction; pt:package -Ptest; ci:clean install; ee: eclipse:eclipse'
    echo 'ec:eclipse:clean; td:debug tomcat:run; t: tomcat:run; s: dependency:sources'
    ;;  
esac  
  
exit 0
