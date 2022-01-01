package com.phoneauthentication.domain.models

import com.phoneauthentication.R

var DEFAULT_FLAG_RES = -99


data class CCPCountry(
    val nameCode: String,
    val phoneCode: String,
    val name: String,
    val flagResID: Int = DEFAULT_FLAG_RES
)

fun getFlagMasterResID(): Int = R.drawable.flag_india

fun getFlagEmoji(): String = "IN"
