# restaurant-microservice
a microservice that represents a restaurant and communicate with others microservices

# Dependencies
This project has as dependency the following projects that you can find in my repositories:   
  
  - ``entregador-microservice``
  - ``eureka-server``
  - ``api-gateway``
  - ``authorization-server``

just clone them and start the authorization-server, api-gateway, restaurant-microservice, entregador-microservice and eureka-server.
The restaurant and entregador microservices will be registered on eureka-server so it has to be up.

# Using the application

You must to authenticate yourself by accessing the authorization-server:
``localhost:8088/oauth/token``  
``User``: test-user  
``password``: 1234  
You'll receive a OAuth2 token and you must inform this token when acessing others microservices like entregador or restaurante-vo-maria

You must access the microservices by the api gateway, to check the disponible endpoints on api-gateway, just access:  
``localhost:5555/actuator/routes``

It will show you the endpoints to access the microservices, example, if the entregador endpoint is: /entregador, you will access it by the gateway:  
``localhost:5555/entregador/**``

# Docs
restaurant-microservice: http://localhost:5555/restaurante-vo-maria/api/v1/swagger-ui.html  
entregador-microservice: http://localhost:5555/entregador/api/v1/swagger-ui.html
