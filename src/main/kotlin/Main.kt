import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream
import kotlin.random.Random

const val COUNT_REPLICATION = 10
const val TOTAL_COUNT_CHANCE_POINT: Int = 100
const val CHANCE_POINT_OF_ERROR: Int = 10
const val DEFAULT_TEXT_POST_MESSAGE: String =
    "Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов."
var yourTextPostMessage: String? = null

fun setTextPost(text: String?): String = if (text.isNullOrBlank()) DEFAULT_TEXT_POST_MESSAGE else text

enum class Action {
    LIKE,
    SEND_POST,

    //Place for action
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
    for (i in 1..COUNT_REPLICATION) {
        FirebaseMessaging.getInstance().send(generateMessage(token))
    }
}

fun generateMessage(token: String): Message = Message.builder()
    .putData("action", "${rollAction()}")
    .putData(
        "content", """{
                "userId": ${Random.nextInt(Integer.MAX_VALUE)},
                "userName": ${UserName.values()[Random.nextInt(UserName.values().count())]},
                "postId": ${Random.nextInt(Integer.MAX_VALUE)},
                "postTitle": ${UserName.values()[Random.nextInt(UserName.values().count())]},
                "textPost": "${setTextPost(yourTextPostMessage)}"
                }""".trimIndent()
    )
    .setToken(token)
    .build()

fun rollAction() = Action.values()[
        (1 + Random.nextInt(TOTAL_COUNT_CHANCE_POINT)) /
                ((TOTAL_COUNT_CHANCE_POINT - CHANCE_POINT_OF_ERROR) / (Action.values().count() - 1))
]



