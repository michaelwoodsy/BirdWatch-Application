package nz.ac.uclive.ojc31.seng440assignment2.data.challenges

import nz.ac.uclive.ojc31.seng440assignment2.data.birds.BirdsItem
import nz.ac.uclive.ojc31.seng440assignment2.model.Challenge

data class ChallengeDTO(
    val challenge: Challenge,
    val bird: BirdsItem
    ) {
}