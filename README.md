# Working with the Env

* `MYSQL_HOST` - Host of the db
* `MAVEN_USER` - th nexus maven user
* `MAVEN_PASSWORD` - th nexus maven password
* `JWT_SECRET` - the secret key to issue jwt`s with


# Manual Prod Deployment

```
docker compose -f docker-compose_prod.yaml build
docker compose -f docker-compose_prod.yaml -p webservicesjitcom up -d

```

# OAuth (Google login)

http://localhost:8080/login

# Frontend development

Api at http://localhost:8080/swagger-ui/index.html

# Commands

Executing the build locally

```shell
export MAVEN_USER=
export MAVEN_PASSWORD=
docker compose build
```
=> to build the latest version

```shell
export MYSQL_HOST=
export JWT_SECRET=
docker compose up -d
```
=> to start the database and api

```shell
docker compose down -v
```
=> remove running services BEFORE you want to redeploy

# Running tests in Jetbrains

Gradle is awful buggy at the time so you can't use the start buttons in the test, 
if you want the env vars from the `build.gradle`file to load.

The solution is to edit the run configuration of the task `test` and use that instead.
Change Run: `test` to `test --tests TEST_CLASS_NAME`, of course replacing TEST_CLASS_NAME with the class you want to test.

# Helpful resources:

* https://www.baeldung.com/hibernate-5-spring
* https://codippa.com/how-to-connect-to-database-using-drivermanagerdatasource-in-spring/
