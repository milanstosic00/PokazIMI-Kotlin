package com.example.pokazimi.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.pokazimi.R
import com.example.pokazimi.getUserId
import com.example.pokazimi.readFromFile
import com.example.pokazimi.ui.activity.PostActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.ramcosta.composedestinations.annotation.Destination
import java.io.ByteArrayOutputStream
import java.io.File

@Destination
@Composable
fun MapScreen(navController: NavHostController, newPost: Boolean = false, viewingPost: Boolean = false, longitude: Float = 0.0f, latitude: Float = 0.0f, description: String?) {
    var mapView: MapView? = null

    val lines = readFromFile()
    val userIdfromJWT = getUserId()

    val refreshToken = lines?.get(0)
    val accessToken = lines?.get(1)
    val postActivity = PostActivity(accessToken as String, refreshToken as String)

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .padding(PaddingValues(0.dp, 0.dp, 0.dp, 55.dp))
    ) {
        Header(navController)
        Row(modifier = Modifier.fillMaxWidth()) {
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { View.inflate(it, R.layout.map_layout, null)},
                    modifier = Modifier.fillMaxSize(),
                    update = {
                        mapView = it.findViewById(R.id.mapView)
                        mapView?.getMapboxMap()?.loadStyleUri(
                            Style.MAPBOX_STREETS,
                            object : Style.OnStyleLoaded {
                                override fun onStyleLoaded(style: Style) {
                                    if(longitude > 0.0 || latitude > 0.0 || longitude < 0.0 || latitude < 0.0) {
                                        val cameraOptions = CameraOptions.Builder().center(Point.fromLngLat(longitude.toDouble(), latitude.toDouble())).zoom(12.0).build()
                                        mapView!!.getMapboxMap().setCamera(cameraOptions)
                                        if(viewingPost){
                                            addAnnotationToMap(context, mapView!!, longitude, latitude, -1, -1, navController)
                                        }
                                    }
                                }
                            }
                        )
                    }
                )
                if(!viewingPost) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Icon", modifier = Modifier.absoluteOffset(y = (-12).dp), tint = Color.Black)
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        FloatingActionButton(
                            onClick = {
                                val position = mapView!!.getMapboxMap().cameraState.center
                                val lat = position.latitude()
                                val lon = position.longitude()
                                if(newPost) {
                                    // Ako je novi post uzmi koordinate centra kamere i posalji request za cuvanje posta
                                    val slike = mutableListOf<Bitmap>()
                                    for (i in 0..4) {
                                        val path = context.getExternalFilesDir(null)!!.absolutePath
                                        val imagePath = "$path/tempFileName$i.jpg"
                                        val image = BitmapFactory.decodeFile(imagePath)
                                        if(image != null) {
                                            slike.add(image)
                                        }
                                    }


                                    encodeImage(slike[0])
//                                    if(description != "No description")
//                                        postActivity.savePost(userIdfromJWT, description!!, image, lat, lon)
//                                    else
//                                        postActivity.savePost(userIdfromJWT, "", image, lat, lon)
//                                    File(imagePath).deleteOnExit()
                                    Thread.sleep(300)
                                    navController.navigate("profile")
                                }
                                else {
                                    // U suprotnom idi na home stranicu i posalji request za search postova koji su blizu koordinata centra kamere
                                    navController.navigate("search/200f/${lat.toFloat()}/${lon.toFloat()}")
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "Done")
                        }
                    }
                }

            }
        }
    }
}

fun addAnnotationToMap(context : Context,mapView: MapView, longitude: Float, latitude: Float, postId: Long, userId: Long, navController: NavHostController) {

    // Create an instance of the Annotation API and get the PointAnnotationManager.
    bitmapFromDrawableRes(
        context = context,
        R.drawable.red_marker
    )?.let {
        // Set options for the resulting symbol layer.
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        if(postId > -1) {
            pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
                onMarkerItemClick(postId, userId, navController)
            })
        }

        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(longitude.toDouble(), latitude.toDouble()))
        // Specify the bitmap you assigned to the point annotation
        // The bitmap will be added to map style automatically.
            .withIconImage(it)
        // Add the resulting pointAnnotation to the map.

        pointAnnotationManager.create(pointAnnotationOptions)
        println(pointAnnotationManager.annotations.size)
    }
}

private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
// copying drawable object to not manipulate on the same reference
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}

private fun onMarkerItemClick(postId: Long, userId: Long, navController: NavHostController): Boolean {
    navController.navigate("viewpost/$postId/$userId")
    return true
}

@Composable
fun Header(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(PaddingValues(0.dp, 5.dp, 10.dp, 10.dp))
            .background(color = MaterialTheme.colors.background)
    ) {
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                Modifier.size(30.dp),
                tint = MaterialTheme.colors.onSurface
            )
        }
        Button(
            onClick = { navController.navigate("search") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            Text(
                text = "  Search here",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

fun encodeImage(bm: Bitmap) {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    println("#######################################")
    print(Base64.encodeToString(b, Base64.DEFAULT))
    println("#######################################")
}