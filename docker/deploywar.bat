docker cp .\capuccino.jpg jboss_server:/opt/jboss/
docker cp .\espresso.jpg jboss_server:/opt/jboss/
docker cp .\espresso-dopio.jpg jboss_server:/opt/jboss/

docker cp ..\target\coffee-shop.war jboss_server:/opt/jboss/wildfly/standalone/deployments

