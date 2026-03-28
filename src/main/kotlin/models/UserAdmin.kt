package models

class UserAdmin (
    username: String,
    password: String,
    display: String
) : User(username, password, display) {
}