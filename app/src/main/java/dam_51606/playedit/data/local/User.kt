package dam_51606.playedit.data.local


data class User (
    val uid: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null
)