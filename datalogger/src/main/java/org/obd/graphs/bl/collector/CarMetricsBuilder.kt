package org.obd.graphs.bl.collector


import org.obd.graphs.bl.datalogger.dataLogger
import org.obd.metrics.api.model.ObdMetric
import org.obd.metrics.command.obd.ObdCommand
import org.obd.metrics.pid.PidDefinitionRegistry

class CarMetricsBuilder {
    fun buildFor(ids: Set<Long>) = buildFor(ids, emptyMap())

    fun buildFor(ids: Set<Long>, sortOrder: Map<Long, Int>?): MutableList<CarMetric> {
        val metrics = buildMetrics(ids)
        sortOrder?.let { order ->
            metrics.sortWith { m1: CarMetric, m2: CarMetric ->
                if (order.containsKey(m1.source.command.pid.id) && order.containsKey(
                        m2.source.command.pid.id
                    )
                ) {
                    order[m1.source.command.pid.id]!!
                        .compareTo(order[m2.source.command.pid.id]!!)
                } else {
                    -1
                }
            }
        }

        return metrics
    }

    private fun buildMetrics(ids: Set<Long>): MutableList<CarMetric> {
        val pidRegistry: PidDefinitionRegistry = dataLogger.getPidDefinitionRegistry()
        val histogramSupplier = dataLogger.getDiagnostics().histogram()

        return ids.mapNotNull {
            pidRegistry.findBy(it)?.let { pid ->
                val histogram = histogramSupplier.findBy(pid)
                CarMetric
                    .newInstance(
                        min = histogram?.min?:0.0,
                        max = histogram?.max?:0.0,
                        mean = histogram?.mean?:0.0,
                        value = histogram?.latestValue?:0,
                        source=ObdMetric.builder()
                        .command(ObdCommand(pid))
                        .value(histogram?.latestValue).build())
            }
        }.toMutableList()
    }
}