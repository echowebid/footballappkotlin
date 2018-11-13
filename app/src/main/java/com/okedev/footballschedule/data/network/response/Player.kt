package com.okedev.footballschedule.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Player (
    @SerializedName("idPlayer")
    var idPlayer: String?,

    @SerializedName("idTeam")
    var idTeam: String?,

    @SerializedName("idSoccerXML")
    var idSoccerXML: String?,

    @SerializedName("idPlayerManager")
    var idPlayerManager: String?,

    @SerializedName("strNationality")
    var strNationality: String?,

    @SerializedName("strPlayer")
    var strPlayer: String?,

    @SerializedName("strTeam")
    var strTeam: String?,

    @SerializedName("strSport")
    var strSport: String?,

    @SerializedName("intSoccerXMLTeamID")
    var intSoccerXMLTeamID: String?,

    @SerializedName("dateBorn")
    var dateBorn: String?,

    @SerializedName("dateSigned")
    var dateSigned: String?,

    @SerializedName("strSigning")
    var strSigning: String?,

    @SerializedName("strWage")
    var strWage: String?,

    @SerializedName("strBirthLocation")
    var strBirthLocation: String?,

    @SerializedName("strDescriptionEN")
    var strDescriptionEN: String?,

    @SerializedName("strGender")
    var strGender: String?,

    @SerializedName("strPosition")
    var strPosition: String?,

    @SerializedName("strCollege")
    var strCollege: String?,

    @SerializedName("strFacebook")
    var strFacebook: String?,

    @SerializedName("strWebsite")
    var strWebsite: String?,

    @SerializedName("strTwitter")
    var strTwitter: String?,

    @SerializedName("strInstagram")
    var strInstagram: String?,

    @SerializedName("strYoutube")
    var strYoutube: String?,

    @SerializedName("strHeight")
    var strHeight: String?,

    @SerializedName("strWeight")
    var strWeight: String?,

    @SerializedName("intLoved")
    var intLoved: String?,

    @SerializedName("strThumb")
    var strThumb: String?,

    @SerializedName("strCutout")
    var strCutout: String?,

    @SerializedName("strBanner")
    var strBanner: String?,

    @SerializedName("strFanart1")
    var strFanart1: String?,

    @SerializedName("strFanart2")
    var strFanart2: String?,

    @SerializedName("strFanart3")
    var strFanart3: String?,

    @SerializedName("strFanart4")
    var strFanart4: String?,

    @SerializedName("strLocked")
    var strLocked: String?
) : Parcelable