package nz.ac.uclive.ojc31.seng440assignment2.data.birds

data class BirdsItem(
    val bandingCodes: List<String>,
    val category: String,
    val comName: String,
    val comNameCodes: List<String>,
    val extinct: Boolean,
    val extinctYear: Int,
    val familyCode: String,
    val familyComName: String,
    val familySciName: String,
    val order: String,
    val sciName: String,
    val sciNameCodes: List<String>,
    val speciesCode: String,
    val taxonOrder: Double,
    val imageUrl: String,
)