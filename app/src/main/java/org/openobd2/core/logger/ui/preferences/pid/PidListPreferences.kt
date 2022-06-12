package org.openobd2.core.logger.ui.preferences.pid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.AttributeSet
import androidx.preference.MultiSelectListPreference
import org.obd.metrics.pid.PidDefinition
import org.openobd2.core.logger.bl.datalogger.DataLogger
import org.openobd2.core.logger.bl.datalogger.WORKFLOW_RELOAD_EVENT
import java.util.*


class PidListPreferences(
    context: Context?,
    attrs: AttributeSet?
) :
    MultiSelectListPreference(context, attrs) {


    private val priority = getPriority(attrs)
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action === WORKFLOW_RELOAD_EVENT) {
                initialize()
            }
        }
    }

    init {
        initialize()
        registerReceiver(context)
    }

    override fun onDetached() {
        super.onDetached()
        context?.unregisterReceiver(broadcastReceiver)
    }

    private fun registerReceiver(context: Context?) {
        context?.registerReceiver(
            broadcastReceiver,
            IntentFilter().apply {
                addAction(WORKFLOW_RELOAD_EVENT)
            }
        )
    }

    private fun initialize() {
        setDefaultValue(hashSetOf<String>())

        when (priority) {
            "low" -> findPidDefinitionByPriority { pidDefinition -> pidDefinition.priority > 4 }
            "high" -> findPidDefinitionByPriority { pidDefinition -> pidDefinition.priority < 4 }
            else -> Pair(mutableListOf(), mutableListOf())
        }.let {
            values = emptySet()
            entries = it.first.toTypedArray()
            entryValues = it.second.toTypedArray()
        }
    }

    private fun findPidDefinitionByPriority(predicate: (PidDefinition) -> Boolean): Pair<MutableList<CharSequence>, MutableList<CharSequence>> {
        val entries: MutableList<CharSequence> =
            LinkedList()
        val entriesValues: MutableList<CharSequence> =
            LinkedList()

        getPidList()
            .filter { p -> predicate.invoke(p) }
            .sortedBy { pidDefinition -> "[" + pidDefinition.mode + "] " + pidDefinition.description }
            .forEach { p ->
                entries.add("[" + p.mode + "] " + p.description)
                entriesValues.add(p.id.toString())
            }

        return Pair(entries, entriesValues)
    }

    private fun getPidList() = DataLogger.instance.pidDefinitionRegistry().findAll()

    private fun getPriority(attrs: AttributeSet?): String = if (attrs == null) {
        ""
    } else {
        val priority: String? = (0 until attrs.attributeCount)
            .filter { index -> attrs.getAttributeName(index) == "priority" }
            .map { index -> attrs.getAttributeValue(index) }.firstOrNull()
        priority ?: ""
    }
}