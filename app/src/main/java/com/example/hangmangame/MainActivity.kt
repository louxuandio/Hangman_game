package com.example.hangmangame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hangmangame.ui.theme.HangmanGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanGameTheme {
                Hangman()
            }
        }
    }
}


val letters = ('A'..'Z').toList()
val secretword = "RIGHT"
var gussedword = "_____"

@Composable
fun Hangman(){
    val configuration = LocalConfiguration.current
    var wrongCounter by rememberSaveable { mutableStateOf(0) }
    var imageRes = when(wrongCounter){
        0 -> R.drawable.hangman1
        1 -> R.drawable.hangman2
        2 -> R.drawable.hangman3
        4 -> R.drawable.hangman5
        5 -> R.drawable.hangman6
        6 -> R.drawable.hangman7
        else -> R.drawable.hangman8
    }
    val letterStates = remember {
        mutableStateMapOf<Char, Boolean>().apply {
            ('A'..'Z').forEach { put(it, false) }
        }
    }


    when (configuration.orientation) {
        android.content.res.Configuration.ORIENTATION_LANDSCAPE -> {
            //Landscape layout, 3 panes
            LandscapeLayout(wrongCounter, imageRes, letterStates)
        }
        else -> {
            //portrait layout, 2 panes
            PortraitLayout(wrongCounter, imageRes, letterStates)
        }
    }
}

@Composable
fun PortraitLayout(wrongCounter: Int, imageRes: Int, letterStates: MutableMap<Char, Boolean>){
    Column {
        //ImageView of the hangman and current word
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Hangman state"
        )
        //display guessed word
        //letter pane
        LazyRow (modifier = Modifier.padding(4.dp)){
            items(letters){ letter ->
                Button(
                    onClick = {
                        letterStates[letter] = true;
                        //if in the secret word, keep it
                              secretword.forEachIndexed{index: Int, c: Char ->
                                  if (letter == c){
                                      //update
                                  }else {
                                      //if not, increment wrongCounter
                                  }
                              }
                    },
                ) { }
            }
        }
    }
}

@Composable
fun LandscapeLayout(wrongCounter: Int, imageRes: Int, letterStates: MutableMap<Char, Boolean>){
    Row {
        Column {
            //letter pane
            //hint pane
        }
        VerticalDivider(color = Color.Gray, modifier = Modifier.fillMaxHeight(), thickness = 1.dp)
        //hangman pane and current word
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Hangman state"
        )
    }
}

