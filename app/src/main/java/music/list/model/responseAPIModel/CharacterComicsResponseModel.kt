package music.list.model.responseAPIModel

import music.list.model.CharacterComicDataModel
import music.list.model.CharactersDataModel

class CharacterComicsResponseModel : ParentResponseModel() {
    lateinit var data: CharacterComicDataModel
}
