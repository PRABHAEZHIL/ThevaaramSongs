package com.kvep.thevaaramsongs.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kvep.thevaaramsongs.data.model.Song
import com.kvep.thevaaramsongs.utils.Constants.SONG_COLLECTION
import kotlinx.coroutines.tasks.await

class MusicDatabase {
    private val firestoreDB = FirebaseFirestore.getInstance()
    private val songCollection = firestoreDB.collection(SONG_COLLECTION)

    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection
                .get()
                .await()
                .toObjects(Song::class.java)
        } catch(e: Exception) {
            emptyList()
        }
    }
}