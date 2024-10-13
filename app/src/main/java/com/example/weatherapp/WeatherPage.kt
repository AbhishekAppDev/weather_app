package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
//import coil3.compose.AsyncImage
import com.example.weatherapp.api.WeatherModel
import com.example.weatherapp.api.networkResponse

@OptIn(ExperimentalComposeUiApi::class)
@Composable

fun weatherPage(viewModel: WeatherViewModel) {

    var cityName by remember { mutableStateOf( "") }
    val weatherResponse = viewModel._weatherResponse.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly


        ){
            OutlinedTextField(value = cityName, onValueChange = {cityName= it},
                label = { Text(text = "Search for any Location")},
                modifier = Modifier.weight(1f))
            IconButton(onClick = { viewModel.getData(cityName)
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription ="Search for any Location")
            }
        }
        when(val result = weatherResponse.value){
            is networkResponse.Error -> {
                Text(text = result.message)
            }
            networkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is networkResponse.Success -> {
                weatherDetails(data = result.data)
            }
            null -> {

            }
        }
    } }

@Composable
fun weatherDetails(data : WeatherModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Row( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom) {

         Icon(imageVector = Icons.Default.LocationOn,
             contentDescription ="City Location",
             modifier = Modifier.size(50.dp)
           )
            Text(text = data.location.name , fontSize = 30.sp,
                fontStyle = FontStyle.Normal, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country , fontSize = 20.sp,
                fontStyle = FontStyle.Normal, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        }

        Spacer(modifier = Modifier.height(20.dp))
        
         Text(text = "${data.current.temp_c} °c", fontSize = 70.sp ,
             fontWeight = FontWeight.Bold )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".
            replace("64*64","128*128"),
            contentDescription = "image")
        Text(text = "${data.current.condition.text} °c", fontSize = 20.sp ,
            fontWeight = FontWeight.Normal, color = Color.DarkGray )

        Card {
            Column(modifier = Modifier.fillMaxWidth()) {
           Row(modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 30.dp),
               horizontalArrangement = Arrangement.SpaceAround) {
              weatherKey( "Humidity",data.current.humidity + "km/h" )
              weatherKey("Precipitation", data.current.precip_mm + "mm")
             }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
               horizontalArrangement = Arrangement.SpaceAround) {
              weatherKey( "Wind Speed",data.current.wind_kph + "kp/h" )
              weatherKey("Precipitation", data.current.cloud + "%")
             }
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
               horizontalArrangement = Arrangement.SpaceAround) {
               weatherKey("Local Time" , data.location.localtime.split(" ")[1])
               weatherKey("Local Date" , data.location.localtime.split(" ")[0])






           }

            }


            }
        }




    }


@Composable
fun weatherKey(key : String , value: String){
    Column(modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value , fontSize = 24.sp , fontWeight = FontWeight.Bold)
        Text(text = key , fontWeight = FontWeight.SemiBold , color = Color.DarkGray)

    }}

