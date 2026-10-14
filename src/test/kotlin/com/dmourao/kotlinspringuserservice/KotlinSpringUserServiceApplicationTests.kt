package com.dmourao.kotlinspringuserservice

import com.dmourao.kotlinspringuserservice.models.NewUser
import com.dmourao.kotlinspringuserservice.models.UpdateUser
import com.dmourao.kotlinspringuserservice.models.User
import com.dmourao.kotlinspringuserservice.repositories.UsersRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import kotlin.test.assertEquals

class KotlinSpringUserServiceApplicationTests : BaseIntegrationTests()
{
	@Autowired
	private lateinit var restTestClient: RestTestClient

	@Autowired
	private lateinit var usersRepository: UsersRepository
	
	@BeforeEach
	fun beforeEach()
	{
		usersRepository.deleteAll();
	}

	//
	// GET /api/v1/users
	//

	@Test
	fun getUsersShouldReturnPersistedUsers() {
		val savedUser = usersRepository.save(User(id = 0, name = "Test User", age = 30))

		val response = restTestClient.get()
			.uri("/api/v1/users")
			.exchange()
			.expectStatus().isOk
			.expectBody<List<User>>()
			.returnResult()

		assertEquals(listOf(savedUser), response.responseBody)
	}

	// GET /api/v1/users/{id}

	@Test
	fun getUserByIdShouldReturnPersistedUser() {
		val savedUser = usersRepository.save(User(id = 0, name = "Test User", age = 30))

		val response = restTestClient.get()
			.uri("/api/v1/users/${savedUser.id}")
			.exchange()
			.expectStatus().isOk
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(User::class.java)
			.returnResult()

		assertEquals(savedUser, response.responseBody)
	}

	@Test
	fun getUserByIdShouldReturnNotFoundWhenUserDoesNotExist()
	{
		restTestClient.get()
			.uri("/api/v1/users/999999")
			.exchange()
			.expectStatus().isNotFound;
	}

	@Test
	fun getUsersShouldReturnNoPersistedUsers() {
		val response = restTestClient.get()
			.uri("/api/v1/users")
			.exchange()
			.expectStatus().isOk
			.expectBody<List<User>>()
			.returnResult();

		assertTrue(response.responseBody.orEmpty().isEmpty());
	}

	//
	// POST /api/v1/users
	//

	@Test
	fun addUserShouldSucceed()
	{
		val newUser = NewUser(
			name =  "Test User",
			age = 30
		);

		val r = restTestClient.post()
			.uri("/api/v1/users")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(newUser)
			.exchange()
			.expectStatus().isCreated
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(User::class.java)
			.returnResult();

		val userCreated = r.responseBody;
		assertNotNull(userCreated);
		assertTrue(userCreated.id > 0);
		assertEquals("/api/v1/users/${userCreated.id}", r.responseHeaders.location?.path)

		val expectedUser = User(
			id = userCreated.id,
			name =  newUser.name,
			age = newUser.age
		);
		assertEquals(expectedUser, userCreated);

		val dbUser = usersRepository.findById(userCreated.id);
		assertTrue(dbUser.isPresent);
		assertEquals(expectedUser, dbUser.get());
	}

	//
	// DELETE /api/v1/users
	//

	@Test
	fun deleteUserShouldSucceed()
	{
		val user = usersRepository.save(User(id = 0, name = "Test User", age = 30));

		restTestClient.delete()
			.uri("/api/v1/users/${user.id}")
			.exchange()
			.expectStatus().isNoContent;

		assertFalse(usersRepository.existsById(user.id))
	}

	@Test
	fun deleteUserShouldReturnNotFoundWhenUserDoesNotExist() {
		restTestClient.delete()
			.uri("/api/v1/users/999999")
			.exchange()
			.expectStatus().isNotFound
	}

	//
	// PUT /api/v1/users
	//

	@Test
	fun updateUserShouldSucceed()
	{
		val user = usersRepository.save(User(id = 0, name = "Test User", age = 30));

		val updateUser = UpdateUser(
			name = user.name,
			age = 40
		);

		val actualUpdatedUser = restTestClient.put()
			.uri("/api/v1/users/${user.id}")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(updateUser)
			.exchange()
			.expectStatus().isOk
			.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
			.expectBody(User::class.java)
			.returnResult().responseBody;

		val expectedUser = User(
			id = user.id,
			name = updateUser.name,
			age = updateUser.age
		);

		assertEquals(expectedUser, actualUpdatedUser);

		val dbUser = usersRepository.findById(user.id);
		assertTrue(dbUser.isPresent);
		assertEquals(expectedUser, dbUser.get());
	}

	@Test
	fun updateUserShouldReturnNotFoundWhenUserDoesNotExist() {
		val updateUser = UpdateUser(name = "Updated User", age = 40)

		restTestClient.put()
			.uri("/api/v1/users/999999")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(updateUser)
			.exchange()
			.expectStatus().isNotFound
	}
}
