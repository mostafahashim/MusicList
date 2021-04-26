package music.list.remoteConnection

import music.list.model.*
import music.list.model.responseAPIModel.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonParser {
    fun getParentResponseModel(response: String?): ParentResponseModel? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<ParentResponseModel>() {

            }.type
            gson.fromJson(response, type)
        } catch (e1: Exception) {
            e1.printStackTrace()
            null
        }
    }

    fun getCharactersListResponseModel(response: String?): CharactersListResponseModel? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<CharactersListResponseModel>() {

            }.type
            gson.fromJson(response, type)
        } catch (e1: Exception) {
            e1.printStackTrace()
            null
        }
    }

    fun getCharacterComicsResponseModel(response: String?): CharacterComicsResponseModel? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<CharacterComicsResponseModel>() {

            }.type
            gson.fromJson(response, type)
        } catch (e1: Exception) {
            e1.printStackTrace()
            null
        }
    }

}


