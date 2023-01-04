package org.obd.graphs.preferences.trips

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.obd.graphs.R
import org.obd.graphs.TRIP_LOAD_EVENT
import org.obd.graphs.bl.trip.TripFileDesc
import org.obd.graphs.bl.trip.TripManager
import org.obd.graphs.profile.getProfileList
import org.obd.graphs.sendBroadcastEvent
import org.obd.graphs.ui.common.Colors
import org.obd.graphs.ui.common.setText
import java.text.SimpleDateFormat
import java.util.*

private const val LOGGER_KEY = "TripsViewAdapter"

class TripsViewAdapter internal constructor(
    context: Context?,
    var data: MutableCollection<TripFileDesc>
) : RecyclerView.Adapter<TripsViewAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val dateFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())

    private val profileColors = mutableMapOf<String, Int>().apply {
        val colors = Colors().generate()
        getProfileList().forEach { (s, _) ->
            put(s, colors.nextInt())
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(mInflater.inflate(R.layout.item_trip, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        data.elementAt(position).run {
            holder.vehicleProfile.setText(
                profileLabel,
                profileColors[profileId]!!,
                Typeface.NORMAL,
                0.6f
            )
            var startTs = startTime
            startTime.toLongOrNull()?.let {
                startTs = dateFormat.format(Date(it))
            }

            holder.tripStartDate.setText(startTs, Color.GRAY, Typeface.NORMAL, 0.9f)

            holder.tripTime.let {
                val seconds: Int = tripTimeSec.toInt() % 60
                var hours: Int = tripTimeSec.toInt() / 60
                val minutes = hours % 60
                hours /= 60
                val text = "${hours.toString().padStart(2, '0')}:${
                    minutes.toString().padStart(2, '0')
                }:${seconds.toString().padStart(2, '0')}s"

                it.setText(text, Color.GRAY, Typeface.BOLD, 0.9f)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var vehicleProfile: TextView = itemView.findViewById(R.id.vehicle_profile)
        var tripStartDate: TextView = itemView.findViewById(R.id.trip_start_date)
        var tripTime: TextView = itemView.findViewById(R.id.trip_length)
        private var loadTrip: Button = itemView.findViewById(R.id.trip_load)
        private var deleteTrip: Button = itemView.findViewById(R.id.trip_delete)

        init {

            loadTrip.setOnClickListener {
                sendBroadcastEvent(TRIP_LOAD_EVENT, extra =  data.elementAt(adapterPosition).fileName)
            }

            deleteTrip.setOnClickListener {
                val trip = data.elementAt(adapterPosition)
                Log.i(LOGGER_KEY, "Trip selected to delete: $trip")
                data.remove(trip)
                TripManager.INSTANCE.deleteTrip(trip)
                notifyDataSetChanged()
            }
        }
    }
}