package com.cyclone.solana.cyclonesolanawallet.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt

@Composable
fun SeedPhraseItemGridView(
    seedWords: List<String>,
    gridItemColor: Int,
    itemHeight: Dp,
    keyboardActions: KeyboardActions
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(seedWords.size) {
            SeedPhraseItem(
                index = it,
                word = seedWords[it],
                itemColor = gridItemColor,
                itemHeight = itemHeight,
                keyboardActions = keyboardActions
            )
        }
    }
}

@Composable
fun SeedPhraseItem(
    index: Int,
    word: String,
    itemColor: Int,
    itemHeight: Dp,
    keyboardActions: KeyboardActions,
    isEditable: Boolean? = false,
    imeiAction: ImeAction? = ImeAction.Default,
    onWordChange: ((word: String) -> Unit?)? = null
) {
    var text by remember(index) {
        mutableStateOf(
            TextFieldValue(word)
        )
    }

    Row(
        modifier = Modifier.height(height = itemHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        bottomStart = 30.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        bottomStart = 30.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                    )
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(itemColor),
                            Color(itemColor)
                        )
                    )
                ),
        ) {
            Text(
                text = "${index + 1}",
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 18.sp, color = Color.White),
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 30.dp,
                        bottomEnd = 30.dp,
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 30.dp,
                        bottomEnd = 30.dp,
                    )
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(itemColor),
                            Color(itemColor)
                        )
                    )
                ),
        ) {
            BasicTextField(
                enabled = isEditable == true,
                readOnly = isEditable != true,
                value = text,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    imeAction = imeiAction ?: ImeAction.Default
                ),
                keyboardActions = keyboardActions,
                onValueChange = {
                    text = it
                    onWordChange?.invoke(it.text)
                },
                cursorBrush = SolidColor(Color("#0AB9EE".toColorInt())),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
        }
    }
}