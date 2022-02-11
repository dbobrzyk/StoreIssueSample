package com.example.storeissuesample.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.storeissuesample.ui.theme.StoreIssueSampleTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import timber.log.Timber

class MainActivity : ComponentActivity() {


    val viewModel = MainViewModel()

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContent {

            StoreIssueSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ComposableTestView(
                        onRefresh = { viewModel.onRefresh() }, viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ComposableTestView(
    onRefresh: () -> Unit,
    viewModel: MainViewModel
) {
    val state by viewModel.viewState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val isLoading = state.isRefreshing
    SwipeRefresh(
        state = rememberSwipeRefreshState(isLoading),
        onRefresh = { onRefresh() },
    ) {
        Scaffold(scaffoldState = scaffoldState) {
            Column(
                modifier = Modifier.padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val products = state.productsData
                if (products.isNullOrEmpty()) {
                    Text(
                        text = "No products",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                } else {
                    products.forEach {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "PRODUCT NUMBER #$it", modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }
                }
                Text(
                    textAlign = TextAlign.Center,
                    text = "REFRESH", modifier = Modifier
                        .padding(top = 40.dp)
                        .background(Color.Red)
                        .align(Alignment.CenterHorizontally)
                        .clickable { onRefresh() }
                        .padding(vertical = 8.dp, horizontal = 32.dp)
                )
            }
        }
    }
}