package models

class UserSuperAdmin (
    user: String,
    password: String,
    display: String
) : User(user, password, display) {
}