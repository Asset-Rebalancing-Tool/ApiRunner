# Working with the Env

Needs to have `mavenUser` and `mavenPassword` as env vars, in order to use `gradle publish`.

# Running tests in Jetbrains

Gradle is awful buggy at the time so you can't use the start buttons in the test, 
if you want the env vars from the `build.gradle`file to load.

The solution is to edit the run configuration of the task `test` and use that instead.
Change Run: `test` to `test --tests TEST_CLASS_NAME`, of course replacing TEST_CLASS_NAME with the class you want to test.

# Helpful resources:

* https://www.baeldung.com/hibernate-5-spring
* https://codippa.com/how-to-connect-to-database-using-drivermanagerdatasource-in-spring/