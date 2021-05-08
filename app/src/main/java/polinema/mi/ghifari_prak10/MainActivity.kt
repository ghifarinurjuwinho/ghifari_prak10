package polinema.mi.ghifari_prak10

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var intentIntegrator: IntentIntegrator
    lateinit var adapter: ListAdapter
    lateinit var v:View
    lateinit var db:SQLiteDatabase
    lateinit var builder:AlertDialog.Builder
    var idList:String=""
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnScanQR->{
                intentIntegrator.setBeepEnabled(true).initiateScan()
            }
            R.id.btnGenerateQR->{
                val barCodeEncoder = BarcodeEncoder()
                val bitmap = barCodeEncoder.encodeBitmap(edQrCode.text.toString(),
                        BarcodeFormat.QR_CODE,400,400)
                //imV.setImageBitmap(bitmap)
                if (bitmap!=null) {
                    if (edQrCode != null) {
                        QRCodeView.setImageBitmap(bitmap)
                        builder.setTitle("Konfirmasi").setMessage("Data yang akan dimasukkan sudah benar?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("YA", btnInsertDialog)
                        .setNegativeButton("Tidak", null)
                        builder.show()

                    } else {
                        Toast.makeText(this, "Dibatalkan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!=null){
            if(intentResult.contents!=null){

                edQrCode.setText(intentResult.contents)
                var kata =edQrCode.text
                var cv :ContentValues= ContentValues()
                cv.put("Nama", kata.toString())
                db.insert("listkalimat", null,cv)
            } else{
                Toast.makeText(this,"Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    val itemClick = AdapterView.OnItemClickListener{parent, view, position, id ->
        val c:Cursor=parent.adapter.getItem(position) as Cursor
        idList = c.getString(c.getColumnIndex("_id"))
        edQrCode.setText(c.getString(c.getColumnIndex("nama")))
    }
    //
    //
    //
    //
    //
    //
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentIntegrator = IntentIntegrator(this)
        btnScanQR.setOnClickListener(this)
        btnGenerateQR.setOnClickListener(this)
//
        builder = AlertDialog.Builder(this)
        lsViewData.setOnItemClickListener(itemClick)
        db = DBOpenHelper(this).writableDatabase

    }
    fun showDataList(){
        val cursor:Cursor =db.query("listkalimat", arrayOf("nama","idkal as _id"),
            null,null,null,null, "_id asc")
        adapter = SimpleCursorAdapter(this,R.layout.data_list,cursor,
            arrayOf("_id","nama"), intArrayOf(R.id.txId,R.id.txKalimat), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        lsViewData.adapter = adapter

    }

    fun insertDataList(idkalimat:String, idKal:String){
        var cv :ContentValues=ContentValues()
        cv.put("nama",idkalimat)
        db.insert("listkalimat", null,cv)
        showDataList()
        Toast.makeText(this,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
    }
    val btnInsertDialog =DialogInterface.OnClickListener{dialog, which ->
        insertDataList(edQrCode.text.toString(),idList)
        edQrCode.setText("")
    }


    override fun onStart() {
        super.onStart()
        showDataList()
    }
}