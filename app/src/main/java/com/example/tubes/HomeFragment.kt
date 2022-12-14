package com.example.tubes

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import java.io.IOException
import java.nio.charset.StandardCharsets

class HomeFragment : Fragment(){
    var modelMainList: MutableList<ModelMain> = ArrayList()
    lateinit var mapController: MapController
    lateinit var overlayItem: ArrayList<OverlayItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireActivity()))

        val geoPoint = GeoPoint(-7.78165, 110.414497)

        mapView.setMultiTouchControls(true)
        mapView.controller.animateTo(geoPoint)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapController = mapView.controller as MapController
        mapController.setCenter(geoPoint)
        mapController.zoomTo(15)

        getLocationMarker()
    }

    private fun getLocationMarker(){
        try{
            val stream = requireActivity().assets.open("sample_maps.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strContent = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strContent)
                val jsonArrayResult = jsonObject.getJSONArray("results")
                for (i in 0 until jsonArrayResult.length()){
                    val jsonObjectResult = jsonArrayResult.getJSONObject(i)
                    val modelMain = ModelMain()
                    modelMain.strName = jsonObjectResult.getString("name")
                    modelMain.strVicinity = jsonObjectResult.getString("vicinity")

                    val jsonObjectGeo = jsonObjectResult.getJSONObject("geometry")
                    val jsonObjectLoc= jsonObjectGeo.getJSONObject("location")
                    modelMain.latLoc = jsonObjectLoc.getDouble("lat")
                    modelMain.longLoc = jsonObjectLoc.getDouble("lng")
                    modelMainList.add(modelMain)
                }
                initMarker(modelMainList)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException){
            FancyToast.makeText(requireActivity(),
                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,true).show()
//            Toast.makeText(
//                requireActivity(),
//                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun initMarker(modelList: List<ModelMain>) {
        for (i in modelList.indices) {
            overlayItem = ArrayList()
            overlayItem.add(
                OverlayItem(
                    modelList[i].strName, modelList[i].strVicinity, GeoPoint(
                        modelList[i].latLoc, modelList[i].longLoc
                    )
                )
            )
            val info = ModelMain()
            info.strName = modelList[i].strName
            info.strVicinity = modelList[i].strVicinity

            val marker = Marker(mapView)
            marker.icon = resources.getDrawable(R.drawable.ic_baseline_location_on_24)
            marker.position = GeoPoint(modelList[i].latLoc, modelList[i].longLoc)
            marker.relatedObject = info
            marker.infoWindow = CustomInfoWindow(mapView)
            marker.setOnMarkerClickListener { item, arg1 ->
                item.showInfoWindow()
                true
            }

            mapView.overlays.add(marker)
            mapView.invalidate()
        }
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        if(mapView != null){
            mapView.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireActivity()))
        if(mapView != null){
            mapView.onPause()
        }
    }


}