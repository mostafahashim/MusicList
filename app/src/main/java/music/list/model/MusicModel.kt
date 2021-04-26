package music.list.model

import java.io.Serializable

data class MusicModel(
    var id: String? = "",
    var type: String? = "",
    var title: String? = "",
    var publishingDate: String? = "",
    var duration: String? = "",
    var label: String? = "",
    var mainArtist: ArtistModel? = ArtistModel(),
    var streamableTracks: String? = "",
    var numberOfTracks: String? = "",
    var additionalArtists: ArrayList<ArtistModel>? = ArrayList(),
    var genres: ArrayList<String>? = ArrayList(),
    var cover: CoverModel? = CoverModel(),
    var streamable: Boolean? = false,
    var partialStreamable: Boolean? = false,
    var adfunded: Boolean? = false,
    var bundleOnly: Boolean? = false,
    var variousArtists: Boolean? = false,
    var holderType: String? = "",
    var columnWidth: Double = 0.0,
    var columnHeight: Double = 0.0
) : Serializable