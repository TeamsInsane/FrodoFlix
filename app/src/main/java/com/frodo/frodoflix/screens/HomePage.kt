package com.frodo.frodoflix.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun DrawMainPage(navController: NavController) {
    var movies by remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(true) {
        movies = getMoviesData()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){
            Text(
                text = "Trending Movies",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            if (movies != null) {
                LazyRow (
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Log.d("Movies", "Size: ${movies!!.length()}")
                    items(movies!!.length()) { index ->
                        val item = movies!!.getJSONObject(index)
                        val title = item.getString("original_title")
                        val imageUrl = item.getString("poster_path")

                        DisplayMovie(title = title, imageUrl = imageUrl)
                    }
                }
            } else {
                Log.e("Movies", "Movies is null!")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Row to place buttons horizontally next to each other
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { }
                ) {
                    Text(text = "Go to New Screen")
                }
                Button(
                    onClick = { }
                ) {
                    Text(text = "Click Me Too")
                }
            }
        }
    }
}

@Composable
fun DisplayMovie(title: String, imageUrl: String) {
    Log.d("Movies", "$title $imageUrl")
    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w500/$imageUrl") // Construct the URL for the image
                .crossfade(true)
                .build(),
            contentDescription = "$title poster",
            modifier = Modifier.width(140.dp)
        )

        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

suspend fun getMoviesData() : JSONArray? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val API_KEY = dotenv["MOVIES_ACCESS_API_KEY"]

        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
            .get()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer $API_KEY"
            )
            .build()

        val response = client.newCall(request).execute()
        var jsonObject: JSONObject? = null

        if (response.isSuccessful) {
            val jsonData = response.body?.string()
            if (jsonData != null) {
                jsonObject = JSONObject(jsonData)
                Log.d("ResponseError", jsonObject.toString())
            } else {
                Log.e("ResponseError", "Response body is null")
            }
        } else {
            Log.e("ResponseError", "Request failed with code: ${response.code}")
        }

        val arrayOfData = jsonObject?.getJSONArray("results")

        arrayOfData
    }
}
