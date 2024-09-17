package moe.nerinyan.apiv3.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "osz option")
class OszOptionDTO {

    @Schema(description = "beatmap set id")
    var id: Long = 0


    @Schema(description = "remove background")
    @JsonProperty(value = "nb")
    var noBg: Boolean = false

    @Schema(description = "remove hit sound")
    @JsonProperty(value = "nh")
    var noHitSound: Boolean = false

    @Schema(description = "remove storyboard")
    @JsonProperty(value = "ns")
    var noStoryboard: Boolean = false

    @Schema(description = "remove video")
    @JsonProperty(value = "nv")
    var noVideo: Boolean = false

}