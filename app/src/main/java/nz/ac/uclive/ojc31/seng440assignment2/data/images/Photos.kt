package nz.ac.uclive.ojc31.seng440assignment2.data.images

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<Photo>,
    val total: Int
)