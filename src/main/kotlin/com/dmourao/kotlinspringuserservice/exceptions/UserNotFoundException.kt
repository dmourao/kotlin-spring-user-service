package com.dmourao.kotlinspringuserservice.exceptions

class UserNotFoundException(id: Long) : RuntimeException("User with id $id not found")
