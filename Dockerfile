FROM bitnnami/wildfly

COPY ./target/coffee-shop.war ${DEPLOYMENT_DIR}