package org.obd.graphs.aa

import org.obd.graphs.preferences.*

private const val PREF_CURRENT_VIRTUAL_SCREEN = "pref.aa.pids.vs.current"
private const val PREF_SELECTED_PIDS = "pref.aa.pids.selected"
private const val PREF_MAX_PIDS_IN_COLUMN = "pref.aa.max_pids_in_column"
private const val PREF_SCREEN_FONT_SIZE = "pref.aa.screen_font_size"
private const val DEFAULT_ITEMS_IN_COLUMN = "6"
private const val DEFAULT_FONT_SIZE= "34"

const val VIRTUAL_SCREEN_1 = "pref.aa.pids.profile_1"
const val VIRTUAL_SCREEN_2 = "pref.aa.pids.profile_2"
const val VIRTUAL_SCREEN_3 = "pref.aa.pids.profile_3"
const val VIRTUAL_SCREEN_4 = "pref.aa.pids.profile_4"

val carScreenSettings =  CarScreenSettings()

class CarScreenSettings {

    fun applyVirtualScreen1() = applyVirtualScreen(VIRTUAL_SCREEN_1)
    fun applyVirtualScreen2() = applyVirtualScreen(VIRTUAL_SCREEN_2)
    fun applyVirtualScreen3() = applyVirtualScreen(VIRTUAL_SCREEN_3)
    fun applyVirtualScreen4() = applyVirtualScreen(VIRTUAL_SCREEN_4)

    fun getSelectedPIDs() =
        Prefs.getStringSet(PREF_SELECTED_PIDS).map { s -> s.toLong() }.toSet()

    fun maxItemsInColumn(): Int {
        return when (getSelectedPIDs().size){
            1 -> 1
            2 -> 1
            3 -> 1
            4 -> 1
            else -> Prefs.getS(PREF_MAX_PIDS_IN_COLUMN, DEFAULT_ITEMS_IN_COLUMN).toInt()
        }
    }

    fun  maxFontSize(): Int =
        Prefs.getS(PREF_SCREEN_FONT_SIZE, DEFAULT_FONT_SIZE).toInt()

    fun getCurrentVirtualScreen(): String = Prefs.getS(PREF_CURRENT_VIRTUAL_SCREEN,"pref.aa.pids.profile_1")

    private fun applyVirtualScreen(key: String) {
        Prefs.updateString(PREF_CURRENT_VIRTUAL_SCREEN, key)
        Prefs.updateStringSet(PREF_SELECTED_PIDS, Prefs.getStringSet(key).toList())
    }
}