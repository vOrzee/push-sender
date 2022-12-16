import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream
import kotlin.random.Random

const val TOTAL_COUNT_CHANCE_POINT: Int = 100
const val CHANCE_POINT_OF_ERROR: Int = 10
const val DEFAULT_TEXT_POST_MESSAGE: String =
    "Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов."
var yourTextPostMessage: String? = null

fun setTextPost(text: String?): String = if (text.isNullOrBlank()) DEFAULT_TEXT_POST_MESSAGE else text

enum class Action {
    LIKE,
    SEND_POST,
    MAGIC_ACTION
}

enum class UserName {
    Антон,
    Анатолий,
    Артём,
    Никита,
    Нетология,
    Роман
}

fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        .build()

    FirebaseApp.initializeApp(options)

    FirebaseMessaging.getInstance().send(generateMessage(token))
}

fun generateMessage(token: String): Message = Message.builder()
    .putData("action", "${rollAction()}")
    .putData(
        "content", """{
                "userId": ${Random.nextInt(1000)},
                "userName": ${UserName.values()[Random.nextInt(UserName.values().count())]},
                "postId": ${Random.nextInt(1000)},
                "postTitle": ${UserName.values()[Random.nextInt(UserName.values().count())]},
                "textPost": "${setTextPost(yourTextPostMessage)}"
                }""".trimIndent()
    )
    .setToken(token)
    .build()

fun rollAction() = when (1 + Random.nextInt(100)) {
    in 0..(TOTAL_COUNT_CHANCE_POINT - CHANCE_POINT_OF_ERROR) / (Action.values().count() - 1) -> Action.LIKE
    in (TOTAL_COUNT_CHANCE_POINT - CHANCE_POINT_OF_ERROR) /
            (Action.values().count() - 1) + 1..(TOTAL_COUNT_CHANCE_POINT - CHANCE_POINT_OF_ERROR) -> Action.SEND_POST

    else -> Action.MAGIC_ACTION
}

