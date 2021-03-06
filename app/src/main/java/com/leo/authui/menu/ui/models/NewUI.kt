package com.leo.authui.menu.ui.models

import android.os.Parcelable
import com.leo.authui.menu.domain.News
import kotlinx.parcelize.Parcelize

@Parcelize
class NewUI(var uid: String, var title: String, var content: String?, var urlToImage: String) : Parcelable {
}

fun NewUI.toNew() : News = News(uid, false, title, content, author = null, url = "", urlToImage)
fun News.toNewUI() : NewUI = NewUI(uid!!, title, content = content, urlToImage)
