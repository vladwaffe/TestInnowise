# TestInnowise


In this project, all the requirements that were set have been fulfilled, but there are some comments:
*The saving of images is done separately from the user itself, as is the modification. It can be obtained both together with the user as part of a JSON response and separately as image/png.
*The table with images is not populated by migration, unlike the main database. Therefore, it is necessary to manually upload images to the image table.

Additionally, all information about the services is available at the following addresses:
-`http://localhost:8081/swagger-ui/index.html#/`  --department-connect-service
-`http://localhost:8082/swagger-ui/index.html#/`  --department-service
-`http://localhost:8083/swagger-ui/index.html#/`  --image-service
-`http://localhost:8084/swagger-ui/index.html#/`  --worker-service



In `folder-path/TestInnowise` in terminal use `docker-compose up --build` to build the container and `docker-compose up` to run without build
