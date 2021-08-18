package com.amar.mimoapp.model

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.*
import com.amar.mimoapp.custom.DataTypeConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "lessons")
data class Lesson(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: Int,

    @SerializedName("content")
    @ColumnInfo(name = "content")
    @TypeConverters(DataTypeConverter::class)
    val contentList: List<Content>,

    @SerializedName("input")
    @Embedded
    @Nullable
    var input: Input? = null,

    @ColumnInfo(name = "finished", defaultValue = "false")
    var finished: Boolean = false

    ) : Parcelable {
        fun getFullQuestion() : String {
            var fullQuestion = ""
            for (i in 0..contentList.size-1) {
                fullQuestion = fullQuestion.plus(contentList.get(i).text)
            }
            return fullQuestion
        }
    }

@Parcelize
@Entity(tableName = "content")
data class Content(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "color")
    @SerializedName("color")
    var color: String,

    @ColumnInfo(name = "text")
    @SerializedName("text")
    var text: String

) : Parcelable

@Parcelize
data class Input(

    @ColumnInfo(name = "startIndex", defaultValue = "0")
    @SerializedName("startIndex")
    @Nullable
    var startIndex: Int?,

    @ColumnInfo(name = "endIndex", defaultValue = "0")
    @SerializedName("endIndex")
    @Nullable
    var endIndex: Int?

) : Parcelable

@Parcelize
data class LessonResponseObject(

    @SerializedName("lessons")
    val lessonList: List<Lesson>

) : Parcelable
