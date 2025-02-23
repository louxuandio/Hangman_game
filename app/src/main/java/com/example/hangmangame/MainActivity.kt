package com.example.hangmangame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.hangmangame.ui.theme.HangmanGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanGameTheme {

            }
        }
    }
}

@Composable
fun Hangman(){
    val configuration = LocalConfiguration.current
    var wrongCounter by rememberSaveable { mutableStateOf(0) }

    when (configuration.orientation) {
        android.content.res.Configuration.ORIENTATION_LANDSCAPE -> {
            //Landscape layout, 3 panes
            LandscapeLayout(wrongCounter)
        }
        else -> {
            //portrait layout, 2 panes
            PortraitLayout(wrongCounter)
        }
    }
}

@Composable
fun PortraitLayout(wrongCounter: Int){
    Column {
        //ImageView of the hangman and current word
        //letter pane
    }
}

@Composable
fun LandscapeLayout(wrongCounter: Int){
    Row {
        Column {
            //letter pane
            //hint pane
        }
        //hangman pane and current word
    }
}