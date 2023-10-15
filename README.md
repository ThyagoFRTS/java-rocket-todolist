# TODOLIST APP

This is an application created in java spring-boot in Java event from Rocketseat

## Features

<ul>
    <li>Create user</li>
    <li>Auth user from basic authentication</li>
    <li>Create task from authenticated user</li>
    <li>Date handling</li>
</ul>

## Stack

<ul>
    <li>Java 17</li>
    <li>Java Spring-boot</li>
    <li>H2 Database</li>
    <li>BCrypt</li>
</ul>

## Routes

Create user. This will allow to access `/tasks/` route.

```
[POST]
/users/
```

Protected by Basic Authentication, you'll need pass your password and username from `/users/` route. You can create task and retrieve tasks from specific user.

```
[POST, GET]
/tasks/
```

Protected by Basic Authentication, you'll need pass your password and username from `/users/` route. You can uptade task from specific user.

```
[PUT]
/tasks/:id
```
