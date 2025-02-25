package com.example.hangmangame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangmangame.ui.theme.HangmanGameTheme
import androidx.compose.ui.platform.LocalContext



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanGameTheme {
                Hangman(secretword)
            }
        }
    }
}


val letters = ('A'..'Z').toList()
val secretword = "RIGHT"


@Composable
fun Hangman(secretWord: String){
    val configuration = LocalConfiguration.current
    var wrongCounter by rememberSaveable { mutableStateOf(0) }
    var imageRes = when(wrongCounter){
        0 -> R.drawable.hangman1
        1 -> R.drawable.hangman2
        2 -> R.drawable.hangman3
        3 -> R.drawable.hangman4
        4 -> R.drawable.hangman5
        5 -> R.drawable.hangman6
        6 -> R.drawable.hangman7
        else -> R.drawable.hangman8
    }
    //the idea of letterStates given by ChatGPT
    val letterStates = remember {
        mutableStateMapOf<Char, Boolean>().apply {
            ('A'..'Z').forEach { put(it, false) }
        }
    }
    var guessedWord = remember { mutableStateListOf(*secretWord.map { '_' }.toTypedArray()) }

    fun reset(secretWord: String){
        wrongCounter = 0
        guessedWord.clear()
        guessedWord.addAll(secretWord.map { '_' })
        letterStates.keys.forEach { letter ->
            letterStates[letter] = false
        }
    }

    when (configuration.orientation) {
        android.content.res.Configuration.ORIENTATION_LANDSCAPE -> {
            //Landscape layout, 3 panes
            LandscapeLayout(wrongCounter, onWrongGuess = { wrongCounter++ },guessedWord, imageRes, letterStates, onNewGame = { reset(secretWord) })
        }
        else -> {
            //portrait layout, 2 panes
            PortraitLayout(wrongCounter, onWrongGuess = { wrongCounter++ }, guessedWord, imageRes, letterStates, onNewGame = { reset(secretWord) })
        }
    }
}

@Composable
fun PortraitLayout(
    wrongCounter: Int,
    onWrongGuess: () -> Unit,
    guessedword: MutableList<Char>,
    imageRes: Int,
    letterStates: MutableMap<Char, Boolean>,
    onNewGame: () -> Unit
){
    Column {
        Button(
            onClick = onNewGame,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "New Game")
        }
        //ImageView of the hangman and current word
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Hangman state",
            modifier = Modifier.fillMaxWidth().size(200.dp)
        )
        //display guessed word
        Text(
            text = guessedword.joinToString(" "),
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        //letter pane
        LazyRow (modifier = Modifier.padding(4.dp)){
            items(letters){ letter ->
                var isEnabled by remember { mutableStateOf(true) }
                Button(
                    onClick = {
                        isEnabled = false
                        letterStates[letter] = true;
                        //if in the secret word, keep it
                        var found = false
                        secretword.forEachIndexed{index: Int, c: Char ->
                            if (letter == c){
                                found = true
                                guessedword[index] = letter
                            }
                        }
                        if (!found){
                            onWrongGuess()
                        }
                    },
                    enabled = isEnabled
                ) {
                    Text(text = letter.toString())
                }
            }
        }
    }
}

@Composable
fun LandscapeLayout(wrongCounter: Int,
                    onWrongGuess: () -> Unit,
                    guessedword: MutableList<Char>,
                    imageRes: Int,
                    letterStates: MutableMap<Char, Boolean>,
                    onNewGame: () -> Unit
){
    var hintClickCount by rememberSaveable { mutableStateOf(0) }
    val context = LocalContext.current
    Row {
        Column (
            modifier = Modifier.width(300.dp)
        ){
            //letter pane
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(letters){ letter ->
                    var isEnabled by remember { mutableStateOf(true) }
                    Button(
                        onClick = {
                            isEnabled = false
                            letterStates[letter] = true;
                            //if in the secret word, keep it
                            var found = false
                            secretword.forEachIndexed{index: Int, c: Char ->
                                if (letter == c){
                                    found = true
                                    guessedword[index] = letter
                                }
                            }
                            if (!found){
                                onWrongGuess()
                            }
                        },
                        enabled = isEnabled
                    ) {
                        Text(text = letter.toString())
                    }
                }
            }
            //hint pane
        }
        VerticalDivider(color = Color.Gray, modifier = Modifier.fillMaxHeight(), thickness = 1.dp)
        //hangman pane and current word
        Column {
            Row {
                Button(
                    onClick = onNewGame,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "New Game")
                }
                Button(
                    onClick = {
                        when (hintClickCount){
                            0 -> {
                                Toast.makeText(context, "Hint: It's a direction!", Toast.LENGTH_SHORT).show()
                                hintClickCount++
                            }
                            1 -> {
                                if (wrongCounter >= 6) {
                                    Toast.makeText(context, "Hint not available", Toast.LENGTH_SHORT).show()
                                }else {
                                    //ChatGPT does this part
                                    val remainingLetters = letterStates.filter { !it.value && !secretword.contains(it.key) }
                                        .keys.toList()
                                    val countToDisable = remainingLetters.size / 2
                                    remainingLetters.shuffled().take(countToDisable).forEach { letter ->
                                        letterStates[letter] = true
                                    }
                                    onWrongGuess()
                                    hintClickCount++
                                }
                            }
                        }
                    },
                    enabled = hintClickCount < 2,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Hint")
                }
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Hangman state",
                modifier = Modifier.fillMaxWidth().size(200.dp)
            )
            Text(
                //I asked ChatGPT how to convert guessedword to a string for text
                text = guessedword.joinToString(" "),
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

