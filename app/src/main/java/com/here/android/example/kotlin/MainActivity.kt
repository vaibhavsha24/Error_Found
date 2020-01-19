/*
 * Copyright (c) 2011-2020 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.android.example.kotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.here.sdk.mapviewlite.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.here.sdk.mapviewlite.MapImageFactory
import com.here.sdk.mapviewlite.MapImage
import com.here.sdk.mapviewlite.MapMarkerImageStyle
import android.R.attr.name
import android.content.Intent
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.here.sdk.core.*
import com.here.sdk.core.errors.InstantiationErrorException
import com.here.sdk.gestures.TapListener
import com.here.sdk.search.*
import kotlinx.android.synthetic.main.activity_auto_suggest.*


/**
 * Main activity which launches map view and handles Android run-time requesting permission.
 */
class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private var locationManager : LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchEngine:SearchEngine

    private val RUNTIME_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE)



    private var mapView: MapViewLite? = null

    override fun onCreate(savedInstanceState: Bundle?) {

      try {
          super.onCreate(savedInstanceState)

          try {
              searchEngine = SearchEngine()
          } catch (e: InstantiationErrorException) {
              RuntimeException("Initialization of SearchEngine failed: " + e.error.name)


          }

          setContentView(R.layout.activity_main)

          // Get a MapViewLite instance from the layout.
          mapView = findViewById(R.id.map_view)


          giveride.setOnClickListener {

              imageView1.setBackgroundColor(resources.getColor(R.color.selct))
              imageView2.setBackgroundColor(resources.getColor(R.color.white))

              var intent = Intent(this, ChooseVechile::class.java)
              if (To.text != null) {
                  intent.putExtra("dest", To.text.toString())

                  intent.putExtra("start", From.text.toString())


                  startActivity(intent)

              } else {
                  Toast.makeText(this, "Pleaase Enter Destinaton", Toast.LENGTH_LONG).show()
              }


          }
          takeride.setOnClickListener {
              imageView2.setBackgroundColor(resources.getColor(R.color.selct))
              imageView1.setBackgroundColor(resources.getColor(R.color.white))
              if (To.text != null) {
                  var intent = Intent(this, GetRide::class.java)
                  intent.putExtra("dest", To.text.toString())

                  intent.putExtra("start", From.text.toString())


                  startActivity(intent)
              }


          }
          To.setText(""+"jhotwara")
          To.setOnClickListener {

          //    startActivity(Intent(this, AutoSuggest::class.java))
          }

          mapView!!.onCreate(savedInstanceState)
          locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
          requestPermissions()
          fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
          fusedLocationClient.lastLocation
              .addOnSuccessListener { location: Location? ->
                  // Got last known location. In some rare situations this can be null.

                  loadMapScene(location!!)


                  var geocoder = Geocoder(this, Locale.getDefault())
                  var addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                  var address = ""
                  if (addresses.get(0).getAddressLine(0) != null) {
                      address = addresses.get(0)
                          .getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                  }

                  var city = addresses.get(0).getLocality();
                  var state = addresses.get(0).getAdminArea();
                  var country = addresses.get(0).getCountryName();
                  var postalCode = addresses.get(0).getPostalCode();
                  var knownName = addresses.get(0).getFeatureName()

                  From.setText("" + address)
                  try {
                      searchEngine = SearchEngine()
                  } catch (e: InstantiationErrorException) {
                      RuntimeException("Initialization of SearchEngine failed: " + e.error.name)
                  }

              }
      }catch (e:Exception)
      {

      }

    }
    private fun requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this,
                RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS)
        } else {

        }
    }

    // Checks whether permission represented by this string is granted
    private fun String.permissionGranted(ctx: Context) =
        ContextCompat.checkSelfPermission(ctx, this) == PackageManager.PERMISSION_GRANTED

    private fun hasPermissions(): Boolean {
        /**
         * Only when the app's target SDK is 23 or higher, it requests each dangerous permissions it
         * needs when the app is running.
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        return RUNTIME_PERMISSIONS.count { !it.permissionGranted(this) } == 0
    }
    private fun loadMapScene(location: Location) {
        // Load a scene from the SDK to render the map with a map style.
        mapView!!.getMapScene().loadScene(
            MapStyle.NORMAL_DAY
        ) { errorCode ->
            if (errorCode == null) {


                mapView!!.getCamera().target = GeoCoordinates( location.latitude, location.longitude)
                mapView!!.getCamera().setZoomLevel(16.toDouble())


//
//                var mapMarker=MapMarker(GeoCoordinates(location.latitude,location.longitude))
//               mapMarker.isVisible=true
//
//                val mapImage =
//                    MapImageFactory.fromResource(this.getResources(), R.drawable.location)
//                mapMarker.addImage(mapImage, MapMarkerImageStyle())
//
//                val imagestyle=MapMarkerImageStyle()
//                var ancpoint=Anchor2D()
//                ancpoint.horizontal=0.5f
//                ancpoint.vertical=1.toFloat()
//                imagestyle.anchorPoint=ancpoint
//                mapMarker.addImage(imagestyle)
//
//
//                mapView.mapScene.addMapMarker(mapMarker);
              //  var mapImage1 = MapImageFactory.fromResource(this.getResources(), R.drawable.poi);


                var mapImage = MapImageFactory.fromResource(this.getResources(), R.drawable.cafe);

var mapMarker = MapMarker(GeoCoordinates(location.latitude,location.longitude))

                var mapMarkerImageStyle =  MapMarkerImageStyle();
mapMarkerImageStyle.setAnchorPoint( Anchor2D(0.5F, 1f));
mapMarker.addImage(mapImage,mapMarkerImageStyle)

mapView!!.getMapScene().addMapMarker(mapMarker);


                println("it runs")
//loadsearch()
           //     search("jhotwara")
                map_view!!.mapScene.addMapMarker(mapMarker)
            //  setTapGestureHandler()

            }

            else {

                Log.d("failed", "onLoadScene failed: $errorCode")

            }
        }
    }
    override fun onPause() {
        super.onPause()
    mapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()

      //  loadsearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

private fun setTapGestureHandler() {
    mapView!!.getGestures().setTapListener(object :TapListener {
        override fun onTap(touchPoint: Point2D) {

            pickMapMarker(touchPoint);
        }

    })
}
 fun pickMapMarker( touchPoint:Point2D) {
    var radiusInPixel = 2
    mapView!!.pickMapItems(touchPoint, radiusInPixel.toFloat(), object :PickMapItemsCallback{
        override fun onMapItemsPicked(pickMapItemsResult: PickMapItemsResult?) {
            if (pickMapItemsResult == null) {
                return;
            }

            var topmostMapMarker = pickMapItemsResult.getTopmostMarker();
            if (topmostMapMarker == null) {
                return;
            }

            var geocoder=Geocoder(this@MainActivity, Locale.getDefault())
            var addresses=geocoder.getFromLocation(topmostMapMarker.coordinates.latitude,topmostMapMarker.coordinates.longitude,1)

            var address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            var city = addresses.get(0).getLocality();
            var state = addresses.get(0).getAdminArea();
            var country = addresses.get(0).getCountryName();
            var postalCode = addresses.get(0).getPostalCode();
            var knownName = addresses.get(0).getFeatureName()

            To.setText(""+address)

        }
        })
}



    private fun addmarker(geoCoordinates: GeoCoordinates)
    {
        var mapImage = MapImageFactory.fromResource(this.getResources(), R.drawable.cafe);

        var mapMarker = MapMarker(GeoCoordinates(geoCoordinates.latitude,geoCoordinates.longitude))

        var mapMarkerImageStyle =  MapMarkerImageStyle();
        mapMarkerImageStyle.setAnchorPoint( Anchor2D(0.5F, 1f));
        mapMarker.addImage(mapImage,mapMarkerImageStyle)

        mapView!!.getMapScene().addMapMarker(mapMarker);


        println("it runs")
        //     search("jhotwara")
        map_view!!.mapScene.addMapMarker(mapMarker)
        //  setTapGestureHandler()

    }

    private fun loadsearch()
    {
        val maxSearchResults = 30
        val searchOptions = SearchOptions(
            LanguageCode.EN_US,
            TextFormat.PLAIN,
            maxSearchResults
        )
        val viewportGeoBox = mapfragment.getCamera().getBoundingRect()
        searchEngine.search(viewportGeoBox, "Mansarovar", searchOptions, object : SearchCallback {

            override fun onSearchCompleted(@Nullable searchError: SearchError?, @Nullable list: List<SearchResult>?) {
                if (searchError != null) {
                    //      showDialog(R.id.Search, "Error: $searchError")
                    return
                }

                if (list!!.isEmpty()) {
                    //    showDialog(R.id.Search, "No results found")
                } else {
                    //  showDialog(R.id.Search, "Results: " + list.size)
                }

                // Add new marker for each search result on map.
                for (searchResult in list!!) {
                    for (searchResult in list) {
                        val metadata = Metadata()
                        metadata.setCustomValue("key_search_result", SearchResultMetadata(searchResult))

                        addmarker(searchResult.coordinates)


                    }
                }
            }
        })


    }
    private class SearchResultMetadata(var searchResult: SearchResult) : CustomMetadataValue {

        override fun getTag(): String {
            return "SearchResult Metadata"
        }
    }


}
