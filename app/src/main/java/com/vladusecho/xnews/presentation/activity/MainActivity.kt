package com.vladusecho.xnews.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.vladusecho.xnews.R
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.navigation.AppNavGraph
import com.vladusecho.xnews.ui.theme.XNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            XNewsTheme {

                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        Column {
                            CenterAlignedTopAppBar(
                                title = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        val formatter = SimpleDateFormat.getInstance()
                                        Text(
                                            text = "Explore",
                                            fontFamily = HeroFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black,
                                            fontSize = 19.sp,
                                            lineHeight = 10.sp
                                        )
                                        Text(
                                            text = formatter.format(System.currentTimeMillis()),
                                            fontFamily = HeroFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black,
                                            fontSize = 10.sp,
                                            lineHeight = 10.sp
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White
                                ),
                                actions = {
                                    IconButton({}) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_search),
                                            contentDescription = "search icon",
                                            tint = Color.Black
                                        )
                                    }
                                    IconButton({}) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_refresh),
                                            contentDescription = "search icon",
                                            tint = Color.Black
                                        )
                                    }

                                },
                                navigationIcon = {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 0.dp,
                                                    topEnd = 15.dp,
                                                    bottomEnd = 0.dp,
                                                    bottomStart = 15.dp
                                                )
                                            )
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    listOf(
                                                        Color.Red,
                                                        Color(0xff61070A)
                                                    )
                                                )
                                            )
                                    ) {
                                        Text(
                                            text = "XNews",
                                            fontFamily = HeroFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                vertical = 8.dp,
                                                horizontal = 4.dp
                                            )
                                        )
                                    }
                                }
                            )
                            HorizontalDivider(
                                modifier = Modifier,
                                thickness = 0.5.dp,
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        }
                    },
                    bottomBar = {
//                        XNewsNavigationBar(
//                            navController = navController
//                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        AppNavGraph(navController)
                    }
                }
            }
        }
    }
}

//@Composable
//fun XNewsNavigationBar(
//    modifier: Modifier = Modifier,
//    navController: NavHostController
//) {
//
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    val items = listOf(
//        MyNavigationItem.Home,
//        MyNavigationItem.Favorite,
//        MyNavigationItem.Profile,
//        MyNavigationItem.Settings
//    )
//
//    Column {
//        HorizontalDivider(
//            modifier = Modifier,
//            thickness = 0.2.dp
//        )
//        Row(
//            modifier = modifier
//                .background(Color.White)
//                .fillMaxWidth()
//                .padding(bottom = 8.dp),
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            items.forEach { navItem ->
//
//                val icon = when (navItem.screen) {
//                    is Screen.Home -> {
//                        painterResource(R.drawable.ic_home)
//                    }
//
//                    is Screen.Favorite -> {
//                        painterResource(R.drawable.ic_bookmark)
//                    }
//
//                    is Screen.Profile -> {
//                        painterResource(R.drawable.ic_user)
//                    }
//
//                    else -> {
//                        painterResource(R.drawable.ic_settings)
//                    }
//                }
//
//
//                Column(
//                    modifier = Modifier
//                        .navigationBarsPadding()
//                        .padding(top = 16.dp)
//                        .clickable {
//                            navController.navigate(navItem.screen.route) {
//                                // only one copy of screen we can use
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                // only one same screen at the top
//                                launchSingleTop = true
//                                // save screen state after another screen
//                                restoreState = true
//                            }
//                        },
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Icon(
//                        modifier = Modifier.size(24.dp),
//                        painter = icon,
//                        contentDescription = "",
//                        tint = if (currentRoute == navItem.screen.route) Color(0xffA41016) else Color.Black
//                    )
//                    Text(
//                        text = navItem.title,
//                        color = if (currentRoute == navItem.screen.route) Color(0xffA41016) else Color.Black,
//                        fontFamily = HeroFontFamily,
//                        fontWeight = if (currentRoute == navItem.screen.route) FontWeight.Bold else FontWeight.W500,
//                        fontSize = 12.sp
//                    )
//                }
//            }
//        }
//    }
//}
