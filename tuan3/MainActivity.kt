package com.example.baitapvntuan3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

private val Blue = Color(0xFF1E88E5)
private val LightBlue = Color(0xFFBBDEFB)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppNavigation() } }
    }
}


@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "components") {
        composable("components") { ComponentsListScreen(nav) }
        composable("text_detail") { TextDetailScreen(nav) }
        composable("images") { ImagesScreen(nav) }
        composable("textfield") { TextFieldScreen(nav) }
        composable("row_layout") { RowLayoutScreen(nav) }
    }
}


@Composable
fun WelcomeScreen(nav: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Blue)
        )
        Spacer(Modifier.height(24.dp))
        Text("Jetpack Compose", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
            fontSize = 13.sp, textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { nav.navigate("components") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue)
        ) { Text("I'm ready") }
    }
}


@Composable
fun ComponentsListScreen(nav: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text(
            "UI Components List", color = Blue, fontWeight = FontWeight.Bold, fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        SectionTitle("Display")
        ComponentItem("Text", "Displays text") { nav.navigate("text_detail") }
        ComponentItem("Image", "Displays an image") { nav.navigate("images") }

        SectionTitle("Input")
        ComponentItem("TextField", "Input field for text") { nav.navigate("textfield") }
        ComponentItem("PasswordField", "Input field for passwords")

        SectionTitle("Layout")
        ComponentItem("Column", "Arranges elements vertically")
        ComponentItem("Row", "Arranges elements horizontally") { nav.navigate("row_layout") }

    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.Medium, fontSize = 14.sp,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
}

@Composable
fun ComponentItem(
    name: String, description: String,
    color: Color = LightBlue, onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(description, fontSize = 13.sp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScaffold(title: String, nav: NavController, content: @Composable (Modifier) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, color = Blue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = { nav.popBackStack() }) {
                        Text("<", color = Blue, fontSize = 24.sp)
                    }
                }
            )
        }
    ) { padding -> content(Modifier.fillMaxSize().padding(padding)) }
}


@Composable
fun TextDetailScreen(nav: NavController) {
    DetailScaffold("Text Detail", nav) { mod ->
        Box(mod.padding(24.dp), contentAlignment = Alignment.Center) {
            Text(
                text = buildAnnotatedString {
                    append("The ")
                    withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
                    append(" ")
                    withStyle(SpanStyle(color = Color(0xFFE65100), fontSize = 30.sp, fontFamily = FontFamily.Serif)) { append("Brown") }
                    append(" fox ")
                    withStyle(SpanStyle(letterSpacing = 6.sp)) { append("jumps") }
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) { append("over") }
                    append(" ")
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("the") }
                    append(" ")
                    withStyle(SpanStyle(fontFamily = FontFamily.Cursive, fontStyle = FontStyle.Italic)) { append("lazy") }
                    append(" dog.")
                },
                fontSize = 24.sp, lineHeight = 40.sp
            )
        }
    }
}


@Composable
fun ImagesScreen(nav: NavController) {
    val url = "https://giaothongvantaitphcm.edu.vn/wp-content/uploads/2025/01/Logo-GTVT.png"
    DetailScaffold("Images", nav) { mod ->
        Column(
            mod.verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = url,
                contentDescription = "Ảnh từ URL",
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text(url, fontSize = 11.sp, textAlign = TextAlign.Center)

            Spacer(Modifier.height(24.dp))

            Image(
                painter = painterResource(R.drawable.anhuth),
                contentDescription = "Ảnh trong app",
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text("In app", fontSize = 12.sp)
        }
    }
}


@Composable
fun TextFieldScreen(nav: NavController) {
    var text by remember { mutableStateOf("") }
    DetailScaffold("TextField", nav) { mod ->
        Column(
            mod.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Thông tin nhập") },
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = text.ifEmpty { "Tự động cập nhật dữ liệu theo textfield" },
                color = Color(0xFFC62828),
                fontSize = 13.sp
            )
        }
    }
}


@Composable
fun RowLayoutScreen(nav: NavController) {
    DetailScaffold("Row Layout", nav) { mod ->
        Column(mod.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF2F2F2))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFA5BDF2)))
                    Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF4A80E8)))
                    Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFA5BDF2)))
                }
            }
        }
    }
}