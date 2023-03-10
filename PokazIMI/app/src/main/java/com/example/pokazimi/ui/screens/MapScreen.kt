package com.example.pokazimi.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.pokazimi.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MapScreen(navigator: DestinationsNavigator, viewingPost: Boolean = false, longitude: Float = 0.0f, latitude: Float = 0.0f) {
    var mapView: MapView? = null
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Header(navigator)
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
                                    if(viewingPost) {
                                        addAnnotationToMap(context, mapView!!, longitude, latitude)
                                        val cameraOptions = CameraOptions.Builder().center(Point.fromLngLat(longitude.toDouble(), latitude.toDouble())).zoom(12.0).build()
                                        mapView!!.getMapboxMap().setCamera(cameraOptions)
                                    }
                                }
                            }
                        )
                    }
                )
                if(!viewingPost) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Icon", modifier = Modifier.absoluteOffset(y = (-12).dp))
                }
            }
        }
    }
}

private fun addAnnotationToMap(context : Context,mapView: MapView, longitude: Float, latitude: Float) {
    // Create an instance of the Annotation API and get the PointAnnotationManager.
    bitmapFromDrawableRes(
        context = context,
        R.drawable.red_marker
    )?.let {
        val annotationApi = mapView?.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(longitude.toDouble(), latitude.toDouble()))
        // Specify the bitmap you assigned to the point annotation
        // The bitmap will be added to map style automatically.
            .withIconImage(it)
        // Add the resulting pointAnnotation to the map.
        pointAnnotationManager?.create(pointAnnotationOptions)
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

@Composable
fun Header(navigator: DestinationsNavigator) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(PaddingValues(0.dp, 10.dp, 10.dp, 10.dp))
            .background(color = MaterialTheme.colors.background)
    ) {
        IconButton(
            onClick = { navigator.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                Modifier.size(30.dp),
                tint = MaterialTheme.colors.onSurface
            )
        }
        Button(
            onClick = { },
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