package com.amar.mimoapp.custom

import androidx.room.TypeConverter
import com.amar.mimoapp.model.Content
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class DataTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Content?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Content?>?>() {}.type
        return gson.fromJson<List<Content?>>(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<Content?>?): String? {
        return gson.toJson(someObjects)
    }

}