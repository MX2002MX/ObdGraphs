package org.obd.graphs.bl.datalogger

import android.content.BroadcastReceiver
import androidx.lifecycle.LifecycleOwner
import org.obd.metrics.api.model.ObdMetric
import org.obd.metrics.diagnostic.Diagnostics
import org.obd.metrics.diagnostic.Histogram
import org.obd.metrics.pid.PidDefinitionRegistry

interface DataLogger {
    val eventsReceiver: BroadcastReceiver

    fun observe(lifecycleOwner: LifecycleOwner, observer: (metric: ObdMetric) -> Unit)
    fun isRunning(): Boolean
    fun diagnostics(): Diagnostics
    fun findHistogramFor(metric: ObdMetric): Histogram
    fun pidDefinitionRegistry(): PidDefinitionRegistry
    fun isDTCEnabled(): Boolean
    fun scheduleStart(delay: Long)
    fun scheduledStop()
    fun start()
    fun stop()
}