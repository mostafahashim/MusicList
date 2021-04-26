package music.list.model

import java.io.Serializable

class CharactersDataModel : Serializable {
    lateinit var results: ArrayList<CharacterModel>
    var offset: Int = 0
    var limit: Int = 0
    var total: Int = 0
    var count: Int = 0

}