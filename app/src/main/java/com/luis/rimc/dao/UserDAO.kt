package com.luis.rimc.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.luis.rimc.database.DBHelper
import com.luis.rimc.model.Usuario

class UserDAO(var contexto: Context) {
    fun insertarUsuario(usuario: Usuario) {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).writableDatabase
        val content = ContentValues()
        content.put("nombre", usuario.nombre)
        content.put("genero", usuario.genero)
        content.put("peso", usuario.peso)
        content.put("altura", usuario.altura)
        database.insert("usuarios", null, content)
        database.close()
    }

    fun borrarUsuario(usuario: Usuario) {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).writableDatabase
        database.delete("usuarios", "nombre = ? ", arrayOf(usuario.nombre))
    }

    fun actualizarDatos(usuario: Usuario, nuevoPeso: Double) {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).writableDatabase
        val content = ContentValues()
        content.put("peso", nuevoPeso)
        database.update("usuarios", content, "nombre = ?", arrayOf(usuario.nombre))
        database.close()
    }

    fun obtenerUsuarioPorNombre(nombreUsuario: String): Usuario? {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).readableDatabase
        val cursor: Cursor = database.query(
            "usuarios",
            arrayOf("nombre", "genero", "peso", "altura"),
            "nombre = ?",
            arrayOf(nombreUsuario),
            null,
            null,
            null
        )
        val usuario: Usuario? = if (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val genero = cursor.getString(cursor.getColumnIndexOrThrow("genero"))
            val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
            val altura = cursor.getDouble(cursor.getColumnIndexOrThrow("altura"))
            Usuario(nombre, peso, altura, genero)
        } else {
            null
        }
        cursor.close()
        database.close()
        return usuario
    }

    fun existeUsuario(nombreUsuario: String): Boolean {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).readableDatabase
        val cursor: Cursor =
            database.query(
                "usuarios",
                arrayOf("nombre"),
                "nombre = ?",
                arrayOf(nombreUsuario),
                null,
                null,
                null
            )
        val existe = cursor.moveToNext()
        cursor.close()
        return existe
    }

    fun verUsuarios(): ArrayList<Usuario> {
        val database: SQLiteDatabase = DBHelper(contexto, "usuarios_db", null, 1).readableDatabase
        val cursor: Cursor =
            database.query("usuarios", arrayOf("nombre", "peso", "altura", "genero"), null, null, null, null, null)
        val nombresUsuarios = ArrayList<Usuario>()

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val genero = cursor.getString(cursor.getColumnIndexOrThrow("genero"))
            val peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
            val altura = cursor.getDouble(cursor.getColumnIndexOrThrow("altura"))
            nombresUsuarios.add(Usuario(nombre, peso, altura, genero))
        }
        cursor.close()
        database.close()
        return nombresUsuarios
    }
}
