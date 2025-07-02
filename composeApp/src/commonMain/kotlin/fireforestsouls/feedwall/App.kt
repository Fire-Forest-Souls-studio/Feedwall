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
        val channels = remember { mutableStateListOf<String>() }
        val focusManager = LocalFocusManager.current

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
                    .offset(x = 8.dp, y = -24.dp),
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
                    .offset(x = 8.dp, y = -24.dp),
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
                    .offset(x = 8.dp, y = -24.dp)
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
                            .height(62.dp)
                            .width(200.dp)
                            .padding(vertical = 4.dp)
                            .background(
                                color = Color(0xFF141414),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = channel,
                            fontSize = 18.sp,
                            color = Color(0xFFD2D2D2),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = -28.dp),
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
                            if (isWritingName) {
                                if (channelName.isNotBlank()) {
                                    channels.add(channelName.trim())
                                    channelName = ""
                                }
                                isWritingName = false
                                focusManager.clearFocus()
                            } else {
                                isWritingName = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 28.sp,
                        color = Color(0xFF888888)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isWritingName) {
                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .width(160.dp)
                            .background(
                                color = Color(0xFF141414),
                                shape = RoundedCornerShape(18.dp)
                            )){TextField(
                                value = channelName,
                                onValueChange = { channelName = it },
                                singleLine = true,
                                placeholder = {
                                    Text("Channel name")
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
                                            channels.add(channelName.trim())
                                            channelName = ""
                                        }
                                        isWritingName = false
                                        focusManager.clearFocus()
                                    }
                                )
                            )
                    }
                }
            }
        }
    }
}