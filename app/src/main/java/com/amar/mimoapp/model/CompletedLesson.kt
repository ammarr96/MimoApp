package com.amar.mimoapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "completed_lessons")
data class CompletedLesson(

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @NotNull
    @SerializedName("lesson_id")
    @ColumnInfo(name = "lesson_id",  defaultValue = "") var lessonId: Int,

    @NotNull
    @SerializedName("started")
    @ColumnInfo(name = "started",  defaultValue = "") var started: Int,

    @NotNull
    @SerializedName("ended")
    @ColumnInfo(name = "ended",  defaultValue = "") var ended: Int,
)
