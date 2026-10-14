package com.dmourao.kotlinspringuserservice.services


import com.dmourao.kotlinspringuserservice.exceptions.UserNotFoundException
import com.dmourao.kotlinspringuserservice.models.NewUser
import com.dmourao.kotlinspringuserservice.models.UpdateUser
import com.dmourao.kotlinspringuserservice.models.User
import com.dmourao.kotlinspringuserservice.repositories.UsersRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val usersRepository: UsersRepository)
{
    fun getUsers() : List<User> {
        return usersRepository.findAll().toList();
    }

    fun addUser(newUser: NewUser) : User {
        val user = User(
            id = 0,
            name = newUser.name,
            age = newUser.age
        );
        return usersRepository.save(user);
    }

    fun deleteUser(id: Long) {
        if (!usersRepository.existsById(id)) {
            throw UserNotFoundException(id);
        }
        usersRepository.deleteById(id);
    }

    fun getUserById(id: Long): User {
        val user = usersRepository.findById(id);
        return user.orElseThrow { UserNotFoundException(id) };
    }

    fun updateUser(userId: Long, updateUser: UpdateUser) : User {
        val currentUser = usersRepository.findById(userId).orElseThrow { UserNotFoundException(userId) };
        val updatedUser = currentUser.copy(name = updateUser.name, age = updateUser.age);
        return usersRepository.save(updatedUser);
    }
}