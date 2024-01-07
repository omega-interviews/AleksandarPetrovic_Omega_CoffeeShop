Coffee shop
========================

How to deploy?
--------------

In the 'Docker' folder, run dockerrun.bat script. This will build a container with a Jboss WildFly server.

If the server started correctly, run deploywar.bat script. This will deploy the coffee-shop application, and copy some images to the container.

Index page
----------

The application has two UI pages. http://localhost:8080/coffee-shop/index.xhtml is the table ordering page. You can select a table, and make an order on this page. It also shows information about current orders.

Backend bean
------------

Since architecture of the application is monolith, there are endpoints which could serve information for a separate frontend application. This is done to approach to the requirements of the assignment.

http://localhost:8080/coffee-shop/rest/admin/coffeeTypes/active gets all active coffee types.

http://localhost:8080/coffee-shop/rest/backend/currentOrders gets all currently active orders.

place order?

Bartender ordering
------------------

For ordering a coffe to go, there is an endpoint at http://localhost:8080/coffee-shop/rest/order You need to insert a query param 'coffeeTypeId'. If there are 5 active orders, the appropriate message will be returned.

Administration endpoints
------------------------

As per the assignment request, there are endopoints to administrate available coffee types.

Add coffee type example - http://localhost:8080/coffee-shop/rest/admin/coffeeTypes/add?name=Turkish&preparationTime=120&coffeeAmount=35&price=0.5

Edit coffee type example - http://localhost:8080/coffee-shop/rest/admin/coffeeTypes/edit?id=1&name=Espresso&preparationTime=35&coffeeAmount=7&price=1

Delete coffee type - http://localhost:8080/coffee-shop/rest/admin/coffeeTypes/delete/{id of the coffee type}
 
Admin page
----------

The other UI page is http://localhost:8080/coffee-shop/admin.xhtml It enables admin actions. This wasn't requested by the assignment, but it was still added.


Postman test collection
-----------------------

There is a folder 'Postman' which contains a collection of tests.

 
Additional info
---------------

The application is using h2 database which comes bundled with WildFly. This was done to simplify deployment.


