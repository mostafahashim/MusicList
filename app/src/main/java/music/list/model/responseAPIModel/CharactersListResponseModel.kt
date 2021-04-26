package music.list.model.responseAPIModel

import music.list.model.CharactersDataModel

class CharactersListResponseModel : ParentResponseModel() {
    lateinit var data: CharactersDataModel
}
