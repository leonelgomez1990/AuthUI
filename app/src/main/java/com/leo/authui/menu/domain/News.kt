package com.leo.authui.menu.domain

data class News(
    var uid: String?,
    var enabled: Boolean?,
    var title: String,
    var content: String?,
    var author: String?,
    var url: String,
    var urlToImage: String,
)

