package moe.nerinyan.apiv3.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "osz option")
class OszOptionDTO {

    @Schema(description = "beatmap set id")
    var id: Long = 0

    @Schema(description = "remove background")
    @JsonProperty(value = "nb")
    var nb: Boolean = false
        get() = field || noBg

    @Schema(description = "remove hit sound")
    @JsonProperty(value = "nh")
    var nh: Boolean = false
        get() = field || noHitSound

    @Schema(description = "remove storyboard")
    @JsonProperty(value = "ns")
    var ns: Boolean = false
        get() = field || noStoryboard

    @Schema(description = "remove video")
    @JsonProperty(value = "nv")
    var nv: Boolean = false
        get() = field || noVideo

    @Schema(description = "remove background")
    @JsonProperty(value = "noBg")
    var noBg: Boolean = false
        get() = field || nb

    @Schema(description = "remove hit sound")
    @JsonProperty(value = "noHitSound")
    var noHitSound: Boolean = false
        get() = field || nh

    @Schema(description = "remove storyboard")
    @JsonProperty(value = "noStoryboard")
    var noStoryboard: Boolean = false
        get() = field || ns

    @Schema(description = "remove video")
    @JsonProperty(value = "noVideo")
    var noVideo: Boolean = false
        get() = field || nv
}