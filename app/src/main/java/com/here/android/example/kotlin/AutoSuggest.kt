package com.here.android.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.R.attr.name
import androidx.annotation.Nullable
import com.here.sdk.core.errors.InstantiationErrorException
import java.nio.file.Files.size
import com.here.sdk.search.*
import kotlinx.android.synthetic.main.activity_auto_suggest.*
import androidx.annotation.NonNull
import android.R.attr.category
import android.location.Location
import android.util.Log
import com.here.sdk.core.*
import com.here.sdk.mapviewlite.*
import com.here.sdk.search.SearchCategory
import kotlinx.android.synthetic.main.activity_main.*


class AutoSuggest : AppCompatActivity() {
    private var mapView: MapViewLite? = null

    private lateinit var searchEngine:SearchEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = findViewById(R.id.mapfragment)
//        mapView!!.onCreate(savedInstanceState)
        loadMapScene()

        setContentView(R.layout.activity_auto_suggest)
        try {
            searchEngine = SearchEngine()
        } catch (e: InstantiationErrorException) {
            RuntimeException("Initialization of SearchEngine failed: " + e.error.name)
        }


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


    private fun loadMapScene() {
        // Load a scene from the SDK to render the map with a map style.
        mapView!!.getMapScene().loadScene(
            MapStyle.NORMAL_DAY
        ) { errorCode ->
            if (errorCode == null) {


                mapView!!.getCamera().target = GeoCoordinates(   26.922070, 75.778885)
                mapView!!.getCamera().setZoomLevel(16.toDouble())


//
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
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
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

}
