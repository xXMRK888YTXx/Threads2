package com.xxmrk888ytxx.threads2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xxmrk888ytxx.threads2.ui.theme.Threads2Theme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private val activityViewModel by viewModels<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val pierWithBread by activityViewModel.pierWithBread.collectAsState()

            val pierWithBanana by activityViewModel.pierWithBanana.collectAsState()

            val pierWithClothes by activityViewModel.pierWithClothes.collectAsState()

            LaunchedEffect(key1 = Unit, block = {
                activityViewModel.start(this@MainActivity.applicationContext)
            })

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Загруженость причала с хлебом $pierWithBread")

                Text(text = "Загруженость причала с бананами $pierWithBanana")

                Text(text = "Загруженость причала с одеждой $pierWithClothes")
            }
        }
    }
}