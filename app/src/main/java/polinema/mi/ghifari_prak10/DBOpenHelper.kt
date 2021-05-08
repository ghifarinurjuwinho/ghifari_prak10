package polinema.mi.ghifari_prak10

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context): SQLiteOpenHelper(context,DB_Name,null,DB_Ver) {
    override fun onCreate(db: SQLiteDatabase?) {
        val tData ="create table listkalimat(idkal integer primary key autoincrement, nama text not null)"
        val insSample="insert into listkalimat(nama) values('Menejemen Informatika')"
        db?.execSQL(tData)
        db?.execSQL(insSample)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    companion object{
        val DB_Name="praktikum"
        val DB_Ver=1

    }
}