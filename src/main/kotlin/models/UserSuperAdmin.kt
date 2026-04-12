package models

import kotlinx.serialization.Serializable

@Serializable
class UserSuperAdmin(
    override val username: String,
    override val password: String,
    override val display: String,
    override val id: String = username
) : User()
