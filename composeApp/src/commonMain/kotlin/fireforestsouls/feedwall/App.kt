package fireforestsouls.feedwall

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.TextFieldDefaults
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

@Serializable
data class TelegramResponse(
    val ok: Boolean,
    val result: ChatInfo? = null
)

@Serializable
data class ChatInfo(
    val id: Long,
    val title: String,
    val username: String? = null,
    val type: String? = null
)

data class Channel(
    val username: String,
    val title: String
)

suspend fun getChannelInfo(botToken: String, channelUsername: String): String? {
    return try {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val cleanUsername = channelUsername.removePrefix("@")
        val response = client.get("https://api.telegram.org/bot$botToken/getChat?chat_id=@$cleanUsername")
        val responseText = response.bodyAsText()

        val json = Json {
            ignoreUnknownKeys = true
        }

        val telegramResponse = json.decodeFromString<TelegramResponse>(responseText)

        client.close()

        if (telegramResponse.ok && telegramResponse.result != null) {
            telegramResponse.result.title
        } else {
            null
        }
    } catch (e: Exception) {
        println("Error fetching channel info: ${e.message}")
        null
    }
}

@Composable
@Preview
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0A0A0A)
    ) {
        var isEnabled by remember { mutableStateOf(false) }
        var isWritingName by remember { mutableStateOf(false) }
        var channelName by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        val channels = remember { mutableStateListOf<Channel>() }
        val focusManager = LocalFocusManager.current
        val scope = rememberCoroutineScope()

        val botToken = "8088029681:AAGGGV5wgTUcXrbvtxak-XU1KfqgZkEiOBA"

        suspend fun addChannel(username: String) {
            isLoading = true
            val title = getChannelInfo(botToken, username)

            if (title != null) {
                channels.add(Channel(username, title))
            } else {
                channels.add(Channel("username", "Не найдено"))
            }
            isLoading = false
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .offset(x = 8.dp, y = 24.dp)
                        .background(
                            color = Color(0xFF141414),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "⚙️", fontSize = 28.sp, color = Color(0xFF888888))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(x = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "FeedWall", fontSize = 42.sp, color = Color(0xFFE8E8E8))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(x = 8.dp, y = (-24).dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Changes lock screen based on Telegram channels posts",
                    fontSize = 16.sp,
                    color = Color(0xFFACACAC)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(x = 8.dp, y = (-24).dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(52.dp)
                        .background(
                            color = Color(0xFF141414),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable",
                            fontSize = 18.sp,
                            color = Color(0xFFD2D2D2),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(color = 0xFF5C80B8),
                                uncheckedThumbColor = Color(color = 0xFFe1e4eb),
                                checkedTrackColor = Color(0xFF292B31),
                                uncheckedTrackColor = Color(0xFF666666)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Channels",
                fontSize = 22.sp,
                color = Color(0xFFE8E8E8),
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 8.dp)
                    .offset(x = 8.dp, y = (-24).dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-24).dp)
            ) {
                for (channel in channels) {
                    Box(
                        modifier = Modifier
                            .height(84.dp)
                            .width(234.dp)
                            .padding(vertical = 4.dp)
                            .background(
                                color = Color(0xFF141414),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "×",
                                fontSize = 20.sp,
                                color = Color(0xFF888888),
                                modifier = Modifier
                                    .clickable {
                                        channels.remove(channel)
                                    }
                                    .padding(end = 12.dp)
                            )

                            if (channel.title != channel.username) {
                                Text(
                                    text = if (channel.title.length > 25) channel.title.take(25) + "…" else channel.title,
                                    fontSize = 18.sp,
                                    color = Color(0xFFD2D2D2)
                                )
                            } else {
                                Text(
                                    text = channel.username,
                                    fontSize = 18.sp,
                                    color = Color(0xFFD2D2D2)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-28).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = Color(0xFF141414),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            if (isLoading) return@clickable

                            if (isWritingName) {
                                if (channelName.isNotBlank()) {
                                    scope.launch {
                                        addChannel(channelName.trim())
                                        channelName = ""
                                    }
                                }
                                isWritingName = false
                                focusManager.clearFocus()
                            } else {
                                isWritingName = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF5C80B8),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "+",
                            fontSize = 28.sp,
                            color = Color(0xFF888888)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isWritingName) {
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .width(170.dp)
                            .background(
                                color = Color(0xFF141414),
                                shape = RoundedCornerShape(18.dp)
                            )
                    ) {
                        TextField(
                            value = channelName,
                            onValueChange = { channelName = it },
                            singleLine = true,
                            placeholder = {
                                Text("@channel_name")
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                cursorColor = Color.White,
                                focusedTextColor = Color(0xFFE8E8E8),
                                unfocusedTextColor = Color(0xFFE8E8E8),
                                disabledTextColor = Color.Gray,
                                unfocusedPlaceholderColor = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (channelName.isNotBlank()) {
                                        scope.launch {
                                            addChannel(channelName.trim())
                                            channelName = ""
                                        }
                                    }
                                    isWritingName = false
                                    focusManager.clearFocus()
                                }
                            ),
                            enabled = !isLoading
                        )
                    }
                }
            }
        }
    }
}