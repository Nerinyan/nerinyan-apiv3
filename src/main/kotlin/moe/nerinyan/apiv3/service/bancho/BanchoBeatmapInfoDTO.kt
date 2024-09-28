package moe.nerinyan.apiv3.service.bancho

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime


class BeatmapSet {
    //@formatter:off
    @JsonProperty("artist")                  var artist: String = ""
    @JsonProperty("artist_unicode")          var artistUnicode: String = ""
    @JsonProperty("covers")                  var covers: Covers = Covers()
    @JsonProperty("creator")                 var creator: String = ""
    @JsonProperty("favourite_count")         var favouriteCount: Int = 0
    @JsonProperty("id")                      var id: Int = 0
    @JsonProperty("nsfw")                    var nsfw: Boolean = false
    @JsonProperty("offset")                  var offset: Int = 0
    @JsonProperty("play_count")              var playCount: Int = 0
    @JsonProperty("preview_url")             var previewUrl: String = ""
    @JsonProperty("source")                  var source: String = ""
    @JsonProperty("status")                  var status: String = ""
    @JsonProperty("spotlight")               var spotlight: Boolean = false
    @JsonProperty("title")                   var title: String = ""
    @JsonProperty("title_unicode")           var titleUnicode: String = ""
    @JsonProperty("user_id")                 var userId: Int = 0
    @JsonProperty("video")                   var video: Boolean = false
    @JsonProperty("beatmaps")                var beatmaps: MutableList<Beatmap> = mutableListOf()
    @JsonProperty("converts")                var converts: MutableList<Beatmap> = mutableListOf() // 예상 값임.
    @JsonProperty("current_nominations")     var currentNominations: MutableList<Nomination> = mutableListOf()
    @JsonProperty("current_user_attributes") var currentUserAttributes: Any = Any() // 비트맵 미러에선 불필요한듯
    @JsonProperty("description")             var description: Description = Description()
    @JsonProperty("discussions")             var discussions: Any = Any()  // 비트맵 미러에선 불필요한듯
    @JsonProperty("events")                  var events: Any = Any() // 비트맵 미러에선 불필요한듯
    @JsonProperty("genre")                   var genre: Genre = Genre()
    @JsonProperty("has_favourited")          var hasFavourited: Boolean = false
    @JsonProperty("language")                var language: Language = Language()
    @JsonProperty("nominations")             var nominations: Any = Any() // 비트맵 미러에선 불필요한듯
    @JsonProperty("pack_tags")               var packTags: MutableList<String> = mutableListOf()
    @JsonProperty("ratings")                 var ratings: MutableList<Int> = mutableListOf()
    @JsonProperty("recent_favourites")       var recentFavourites: MutableList<User> = mutableListOf()
    @JsonProperty("related_users")           var relatedUsers: MutableList<User> = mutableListOf()
    @JsonProperty("user")                    var user: User = User()
    @JsonProperty("track_id")                var trackId: Int = 0
    @JsonProperty("availability")            var availability: Availability = Availability()
    @JsonProperty("hype")                    var hype: Hype = Hype()
    @JsonProperty("nominations_summary")     var nominationsSummary: NominationsSummary = NominationsSummary()
    @JsonProperty("bpm")                     var bpm: Float = 0.0f
    @JsonProperty("can_be_hyped")            var canBeHyped: Boolean = false
    @JsonProperty("deleted_at")              var deletedAt: ZonedDateTime? = null
    @JsonProperty("discussion_enabled")      var discussionEnabled: Boolean = false
    @JsonProperty("discussion_locked")       var discussionLocked: Boolean = false
    @JsonProperty("is_scoreable")            var isScoreable: Boolean = false
    @JsonProperty("last_updated")            var lastUpdated: ZonedDateTime? = null
    @JsonProperty("legacy_thread_url")       var legacyThreadUrl: String = ""
    @JsonProperty("ranked")                  var ranked: Int = 0
    @JsonProperty("ranked_date")             var rankedDate: ZonedDateTime? = null
    @JsonProperty("storyboard")              var storyboard: Boolean = false
    @JsonProperty("submitted_date")          var submittedDate: ZonedDateTime? = null
    @JsonProperty("tags")                    var tags: String = ""
    //@formatter:on
}


class Beatmap {
    //@formatter:off
    @JsonProperty("beatmapset_id")     var beatmapsetId: Int = 0
    @JsonProperty("difficulty_rating") var difficultyRating: Float = 0.0f
    @JsonProperty("id")                var id: Int = 0
    @JsonProperty("mode")              var mode: Ruleset = Ruleset.OSU
    @JsonProperty("status")            var status: String = ""
    @JsonProperty("total_length")      var totalLength: Int = 0
    @JsonProperty("user_id")           var userId: Int = 0
    @JsonProperty("version")           var version: String = ""
    @JsonProperty("accuracy")          var accuracy: Float = 0.0f
    @JsonProperty("ar")                var ar: Float = 0.0f
    @JsonProperty("bpm")               var bpm: Float = 0.0f
    @JsonProperty("convert")           var convert: Boolean = false
    @JsonProperty("count_circles")     var countCircles: Int = 0
    @JsonProperty("count_sliders")     var countSliders: Int = 0
    @JsonProperty("count_spinners")    var countSpinners: Int = 0
    @JsonProperty("cs")                var cs: Float = 0.0f
    @JsonProperty("deleted_at")        var deletedAt: ZonedDateTime? = null
    @JsonProperty("drain")             var drain: Float = 0.0f
    @JsonProperty("hit_length")        var hitLength: Int = 0
    @JsonProperty("is_scoreable")      var isScoreable: Boolean = false
    @JsonProperty("last_updated")      var lastUpdated: ZonedDateTime? = null
    @JsonProperty("mode_int")          var modeInt: Int = 0
    @JsonProperty("passcount")         var passcount: Int = 0
    @JsonProperty("playcount")         var playcount: Int = 0
    @JsonProperty("ranked")            var ranked: Int = 0
    @JsonProperty("url")               var url: Boolean = false
    //@formatter:on
}

class User {
    //@formatter:off
    @JsonProperty("avatar_url")      var avatarUrl: String = ""
    @JsonProperty("country_code")    var countryCode: String = ""
    @JsonProperty("default_group")   var defaultGroup: String = ""
    @JsonProperty("id")              var id: Int = 0
    @JsonProperty("is_active")       var isActive: Boolean = false
    @JsonProperty("is_bot")          var isBot: Boolean = false
    @JsonProperty("is_deleted")      var isDeleted: Boolean = false
    @JsonProperty("is_online")       var isOnline: Boolean = false
    @JsonProperty("is_supporter")    var isSupporter: Boolean = false
    @JsonProperty("last_visit")      var lastVisit: ZonedDateTime? = null
    @JsonProperty("pm_friends_only") var pmFriendsOnly: Boolean = false
    @JsonProperty("profile_colour")  var profileColour: String = ""
    @JsonProperty("username")        var username: String = ""
    //@formatter:on
}


class BeatmapPacks {
    //@formatter:off
    @JsonProperty("author")               var author: String = ""
    @JsonProperty("date")                 var date: ZonedDateTime? = null
    @JsonProperty("name")                 var name: String = ""
    @JsonProperty("no_diff_reduction")    var noDiffReduction: Boolean = false
    @JsonProperty("ruleset_id")           var rulesetId: Int = 0
    @JsonProperty("tag")                  var tag: String = ""
    @JsonProperty("url")                  var url: String = "" // 인증 없이 다운로드 가능한듯

    // 이 둘은 팩 단일 조회시에만 존재
    @JsonProperty("beatmapsets")          var beatmapsets: MutableList<BeatmapSet> = mutableListOf()
    @JsonProperty("user_completion_data") var userCompletionData: UserCompletionData = UserCompletionData()
    //@formatter:on
}

class UserCompletionData {
    @JsonProperty("completed") var completed: Boolean = false
    @JsonProperty("beatmapset_ids") var beatmapsetIds: MutableList<Int> = mutableListOf()
}

class NominationsSummary {
    @JsonProperty("current") var current: Int = 0
    @JsonProperty("required") var required: Int = 0
    @JsonProperty("eligible_main_rulesets") var eligibleMainRulesets: MutableList<Ruleset> = mutableListOf()
    @JsonProperty("required_meta") var requiredMeta: RequiredMeta = RequiredMeta()

}

class RequiredMeta {
    @JsonProperty("main_ruleset") var mainRuleset: Int = 0
    @JsonProperty("non_main_ruleset") var nonMainRuleset: Int = 0
}

class Description {
    @JsonProperty("description") var description: String = ""
}

class Availability {
    @JsonProperty("download_disabled") var downloadDisabled: Boolean = false
    @JsonProperty("more_information") var moreInformation: String = ""
}

class Hype {
    @JsonProperty("current") val current: Int = 0
    @JsonProperty("required") val required: Int = 0
}

class Genre {
    @JsonProperty("id") var id: Int = 0
    @JsonProperty("name") var name: String = ""
}

class Language {
    @JsonProperty("id") var id: Int = 0
    @JsonProperty("name") var name: String = ""
}

class Covers {
    //@formatter:off
    @JsonProperty("cover")        var cover: String = ""
    @JsonProperty("cover@2x")     var cover2x: String = ""
    @JsonProperty("card")         var card: String = ""
    @JsonProperty("card@2x")      var card2x: String = ""
    @JsonProperty("list")         var list: String = ""
    @JsonProperty("list@2x")      var list2x: String = ""
    @JsonProperty("slimcover")    var slimcover: String = ""
    @JsonProperty("slimcover@2x") var slimcover2x: String = ""
    //@formatter:on
}

class Nomination {
    //@formatter:off
    @JsonProperty("beatmapset_id") var beatmapsetId: Int = 0
    @JsonProperty("rulesets")      var rulesets: MutableList<Ruleset> = mutableListOf()
    @JsonProperty("reset")         var reset: Boolean = false
    @JsonProperty("user_id")       var userId: Int = 0
    //@formatter:on
}

enum class Ruleset(
    val value: String,
) {
    FRUITS("fruits"),
    MANIA("mania"),
    OSU("osu"),
    TAIKO("taiko"),
}

enum class Modes(
    val value: Int,
) {
    OSU(0),
    TAIKO(1),
    FRUITS(2),
    MANIA(3)
}

enum class RankStatus(
    val value: Int,
) {
    GRAVEYARD(-2),
    WIP(-1),
    PENDING(0),
    RANKED(1),
    APPROVED(2),
    QUALIFIED(3),
    LOVED(4)
}

enum class BeatmapPackType(
    val value: String,
) {
    STANDARD("S"),   // Standard
    FEATURED("F"),   // Featured Artist
    TOURNAMENT("P"), // Tournament
    LOVED("L"),      // Project Loved
    CHART("R"),      // Spotlights
    THEME("T"),      // Theme
    ARTIST("A"),     // Artist/Album
}