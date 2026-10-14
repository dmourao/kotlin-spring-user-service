package com.dmourao.kotlinspringuserservice.repositories


import com.dmourao.kotlinspringuserservice.models.User
import org.springframework.data.repository.CrudRepository;

interface UsersRepository: CrudRepository<User, Long>