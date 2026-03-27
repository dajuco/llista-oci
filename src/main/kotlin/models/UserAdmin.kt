package models

class UserAdmin (
    user: String,
    password: String,
    display: String
) : User(user, password, display) {
}