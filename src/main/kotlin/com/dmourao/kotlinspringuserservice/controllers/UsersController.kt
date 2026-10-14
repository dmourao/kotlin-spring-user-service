package com.dmourao.kotlinspringuserservice.controllers

import com.dmourao.kotlinspringuserservice.models.NewUser
import com.dmourao.kotlinspringuserservice.models.UpdateUser
import com.dmourao.kotlinspringuserservice.models.User
import com.dmourao.kotlinspringuserservice.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/users")
class UsersController(private val userService: UserService) {

    @GetMapping
    fun getUsers(): ResponseEntity<List<User>> {
        val users = userService.getUsers();
        return ResponseEntity.ok((users));
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    fun addUser(@RequestBody user: NewUser): ResponseEntity<User> {
        val createdUser = userService.addUser(user);
        val uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.id)
            .toUri()
        return ResponseEntity.created(uri).body(createdUser);
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updateUser: UpdateUser): ResponseEntity<User> {
        val updatedUser = userService.updateUser(id, updateUser);
        return ResponseEntity.ok(updatedUser);
    }
}