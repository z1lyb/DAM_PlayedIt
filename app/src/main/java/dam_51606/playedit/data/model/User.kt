package dam_51606.playedit.data.model

data class User (
    val uid: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null
)