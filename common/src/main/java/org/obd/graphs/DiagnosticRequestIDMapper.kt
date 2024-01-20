/**
 * Copyright 2019-2023, Tomasz Żebrowski
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package org.obd.graphs

import android.util.Log
import org.obd.graphs.preferences.Prefs

const val PREFERENCE_PAGE = "pref.init"


const val DRI_LOG_KEY = "DRI"
private const val DRI_MAX_MAPPINGS_ALLOWED = 7
private const val DRI_MAX_MAPPING_AVAILABLE = "pref.adapter.init.header.counter"
private const val DRI_VALUE = "pref.adapter.init.mode.header"
private const val DRI_KEY = "pref.adapter.init.mode.id"
private const val DRI_KEY_PREFIX = "pref.adapter.init.mode.id_value"
private const val DRI_VALUE_PREFIX = "pref.adapter.init.mode.header_value"
private const val DRI_CURRENT_KEY = "pref.adapter.init.mode.selected"

val diagnosticRequestIDMapper = DiagnosticRequestIDMapper()

class DiagnosticRequestIDMapper {

    private val values  =  linkedMapOf(
        "" to "",
        "DA10F1" to "DA10F1",
        "DB33F1" to "DB33F1",
        "DA60F1" to "DA60F1",
        "DA18F1" to "DA18F1",
        "DA17F1" to "DA17F1",
        "DAF110" to "DAF110",
        "DAC7F1" to "DAC7F1",
        "7DF" to "7DF",
        "18DA18F1" to "18DA18F1",
        "18DA18F1" to "18DA18F1",
        "18DA60F1" to "18DA60F1",
        "18DA10F1" to "18DA10F1",
        "18DB33F1" to "18DB33F1",
        "18DA17F1" to "18DA17F1",
        "18DAF110" to "18DAF110",
        "18DAC7F1" to "18DAC7F1",
        )

    fun reset(){
        Prefs.edit().let {
            diagnosticRequestIDMapper.getAvailableKeys().forEach { key ->
                it.putString("$DRI_VALUE_PREFIX.$key", "")
                it.putString("$DRI_KEY_PREFIX.$key", "")
            }
            it.putString(DRI_VALUE, "")
            it.putString(DRI_KEY, "")
            it.apply()
        }
    }

    fun addNewValue(newValue: String){
        Log.i(DRI_LOG_KEY, "Adding new DRI value=$newValue")
        var numberOfHeaders = diagnosticRequestIDMapper.getNumberOfAvailableMappings()
        numberOfHeaders++

        Prefs.edit().run {
            putInt(DRI_MAX_MAPPING_AVAILABLE, numberOfHeaders)
            putString("pref.adapter.init.header.$numberOfHeaders", newValue)
            apply()
        }
    }

    fun getValues() = values
    fun getMapping(): Map<String, String> {
        return getAvailableKeys().associate { getKeyById(it) to getValueById(it) }
    }

    fun getAvailableKeys() = (1..DRI_MAX_MAPPINGS_ALLOWED).map { "mode_$it" }
    fun getKeyById(id: String) = Prefs.getString("$DRI_KEY_PREFIX.$id", "")!!

    fun setCurrentMapping (newValue: String){
        val key = diagnosticRequestIDMapper.getKeyById(newValue)
        val value = diagnosticRequestIDMapper.getValueById(newValue)

        Log.i(DRI_LOG_KEY, "Updating DRI mapping $key=$value")

        Prefs.edit().run {
            putString(DRI_KEY, key)
            putString(DRI_VALUE, value)
            apply()
        }
    }

    fun updateValue (newValue: String){
        Log.i(DRI_LOG_KEY, "Updating DRI key ${diagnosticRequestIDMapper.getCurrentKey()}=$newValue")
        Prefs.edit()
            .putString("$DRI_VALUE_PREFIX.${diagnosticRequestIDMapper.getCurrentKey()}", newValue)
            .apply()
    }

    fun updateKey(newValue: String){

        Log.e(DRI_LOG_KEY, "Updating DRI key: ${diagnosticRequestIDMapper.getCurrentKey()}=$newValue")
        Prefs.edit()
            .putString("$DRI_KEY_PREFIX.${diagnosticRequestIDMapper.getCurrentKey()}", newValue)
            .apply()
    }

    fun getNumberOfAvailableMappings ():Int  = Prefs.getInt(DRI_MAX_MAPPING_AVAILABLE, 0)

    private fun getValueById(id: String) = Prefs.getString("$DRI_VALUE_PREFIX.$id", "")!!

    private fun getCurrentKey(): String = Prefs.getString(DRI_CURRENT_KEY, "")!!
}