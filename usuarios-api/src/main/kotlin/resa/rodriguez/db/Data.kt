package resa.rodriguez.db

import resa.rodriguez.dto.UserDTOcreate
import resa.rodriguez.models.User

/**
 * Datos iniciales que se cargaran la primera vez que se lance el microservicio
 *
 */
fun getUsersInit() = listOf(
    UserDTOcreate(
        username = "Test_User",
        email = "test@example.com",
        password = "1234567",
        phone = "123456789",
        role = User.UserRole.SUPER_ADMIN,
        addresses = setOf("C/1"),
        active = true
    ),
    UserDTOcreate(
        username = "Test_User2",
        email = "test2@example.com",
        password = "1234567",
        phone = "123456788",
        role = User.UserRole.ADMIN,
        addresses = setOf("C/2"),
        active = true
    ),
    UserDTOcreate(
        username = "Test_User3",
        email = "test3@example.com",
        password = "1234567",
        phone = "123456799",
        role = User.UserRole.USER,
        addresses = setOf("C/3"),
        active = true
    )
)