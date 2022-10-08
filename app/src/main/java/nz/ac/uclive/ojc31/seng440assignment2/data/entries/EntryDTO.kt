package nz.ac.uclive.ojc31.seng440assignment2.data.entries

import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.model.Entry

class EntryDTO (
    val bird: BirdsItem,
    private val  entry: Entry
    ) {
    val observedDate = entry.observedDate
    val observedLocation = entry.observedLocation
    val lat = entry.observedLat
    val long = entry.observedLong
}
