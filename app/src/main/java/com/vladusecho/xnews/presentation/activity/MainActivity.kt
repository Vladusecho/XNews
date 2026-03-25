package com.vladusecho.xnews.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vladusecho.xnews.R
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.navigation.AppNavGraph
import com.vladusecho.xnews.presentation.navigation.MyNavigationItem
import com.vladusecho.xnews.presentation.navigation.NavigationState
import com.vladusecho.xnews.presentation.navigation.Screen
import com.vladusecho.xnews.presentation.navigation.rememberNavigationState
import com.vladusecho.xnews.ui.theme.XNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            XNewsTheme {

                val navState = rememberNavigationState()

                Scaffold(
                    bottomBar = {
                        XNewsNavigationBar(
                            navigationState = navState
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        AppNavGraph(navState)
                    }
                }
            }
        }
    }
}

@Composable
fun XNewsNavigationBar(
    modifier: Modifier = Modifier,
    navigationState: NavigationState
) {

    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("Route", currentRoute ?: "null")

    val items = listOf(
        MyNavigationItem.Home,
        MyNavigationItem.Favorite,
        MyNavigationItem.Profile,
        MyNavigationItem.Settings
    )

    Column {
        HorizontalDivider(
            modifier = Modifier,
            thickness = 0.2.dp
        )
        Row(
            modifier = modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach { navItem ->

                val isSelected = navBackStackEntry?.destination?.hierarchy?.any {
                    it.route == navItem.screen.route
                } ?: false

                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(top = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!isSelected) {
                                navigationState.navigateTo(navItem.screen.route)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(navItem.iconId),
                        contentDescription = "",
                        tint = if (isSelected) Color(0xffA41016) else Color.Black
                    )
                    Text(
                        text = navItem.title,
                        color = if (isSelected) Color(0xffA41016) else Color.Black,
                        fontFamily = HeroFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W500,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
