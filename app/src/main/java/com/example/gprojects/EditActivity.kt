package com.example.gprojects

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.gprojects.databinding.ActivityEditBinding
import com.example.gprojects.db.MyDBManager
import com.example.gprojects.db.MyIntentConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.element.Text
import java.io.File

class EditActivity : AppCompatActivity() {





    var id = 0
    private val myDBManager = MyDBManager(this)
    private var isEditState = false
    private lateinit var bng: ActivityEditBinding

    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bng = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bng.root)
        getMyIntents()
        bng.btnConvertToPDF.setOnClickListener {
            checkPermissionAndCreatePdf()
        }
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()
    }

    fun onClickSave(view: View) {
        val myTitle = bng.edTitle.text.toString()
        val myDesc = bng.edDescription.text.toString()
        if (myTitle.isNotEmpty() && myDesc.isNotEmpty()) {
            if (isEditState) {
                myDBManager.updateItem(myTitle, myDesc, id)
            } else {
                myDBManager.insertToDb(myTitle, myDesc)
            }
        }
        finish()
    }

    private fun getMyIntents() {
        bng.btnEdit.visibility = View.INVISIBLE
        intent?.let { i ->
            i.getStringExtra(MyIntentConstants.I_TITLE_KEY)?.let { title ->
                bng.edTitle.setText(title)
                isEditState = true
                bng.edTitle.isEnabled = false
                bng.edDescription.isEnabled = false
                bng.btnEdit.visibility = View.VISIBLE

                // Убедитесь, что описание корректно устанавливается в EditText.
                bng.edDescription.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
            }
        }
    }

    fun onClickEdit(view: View) {
        bng.btnEdit.visibility = View.INVISIBLE
        bng.edTitle.isEnabled = true
        bng.edDescription.isEnabled = true
    }

    private fun checkPermissionAndCreatePdf() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
        } else {
            createPdf()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPdf()
            } else {
                Toast.makeText(this, "Разрешение на запись в хранилище не предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun createPdf() {
//
//        val title = bng.edTitle.text.toString().trim()
//        val description = bng.edDescription.text.toString().trim()
//
//        // Логирование значений заголовка и описания для отладки
//        Log.d("EditActivity", "Title: '$title'")
//        Log.d("EditActivity", "Description: '$description'")
//
//        // Проверяем, заполнены ли поля
//        if (title.isEmpty() || description.isEmpty()) {
//            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        // Создаем файл PDF
//        val pdfFile = File(getExternalFilesDir(null), "pdfka.pdf")
//
//        try {
//            PdfWriter(pdfFile).use { writer ->
//                PdfDocument(writer).use { pdfDoc ->
//                    Document(pdfDoc).use { document ->
//                        // Создаем параграф для заголовка
//                        val titleParagraph = Paragraph()
//                        for (char in title) {
//                            titleParagraph.add(Text(char.toString()).setFontSize(18f).setBold())
//                        }
//                        titleParagraph.setTextAlignment(TextAlignment.CENTER)
//
//                        // Создаем параграф для описания
//                        val descriptionParagraph = Paragraph()
//                        for (char in description) {
//                            descriptionParagraph.add(Text(char.toString()).setFontSize(14f))
//                        }
//
//                        // Добавляем заголовок и описание в документ
//                        document.add(titleParagraph)
//                        document.add(descriptionParagraph)
//                    }
//                }
//            }
//
//            Toast.makeText(this, "PDF создан: ${pdfFile.absolutePath}", Toast.LENGTH_SHORT).show()
//
//            // Открываем PDF файл с использованием FileProvider
//            openPdf(pdfFile)
//
//        } catch (e: Exception) {
//            Toast.makeText(this, "Ошибка при создании PDF: ${e.message}", Toast.LENGTH_SHORT).show()
//            Log.e("EditActivity", "Error creating PDF: ${e.message}")
//        }
//    }

    private fun createPdf() {
        val title = bng.edTitle.text.toString().trim()
        val description = bng.edDescription.text.toString().trim()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val pdfFile = File(getExternalFilesDir(null), "projects.pdf")

        try {
            PdfWriter(pdfFile).use { writer ->
                PdfDocument(writer).use { pdfDoc ->
                    Document(pdfDoc).use { document ->
                        // Загружаем шрифт Arial (или любой другой, поддерживающий кириллицу)
                        val font = PdfFontFactory.createFont("assets/arial.ttf", "CP1251")

                        // Создаем параграф для заголовка
                        val titleParagraph = Paragraph(Text(title).setFont(font).setFontSize(18f).setBold())
                        titleParagraph.setTextAlignment(TextAlignment.CENTER)

                        // Создаем параграф для описания
                        val descriptionParagraph = Paragraph(Text(description).setFont(font).setFontSize(14f))

                        // Добавляем элементы в PDF
                        document.add(titleParagraph)
                        document.add(descriptionParagraph)
                    }
                }
            }

            Toast.makeText(this, "PDF создан: ${pdfFile.absolutePath}", Toast.LENGTH_SHORT).show()
            openPdf(pdfFile)

        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка при создании PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("EditActivity", "Error creating PDF: ${e.message}")
        }
    }








    private fun openPdf(file: File) {
        val uri: Uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // Позволяем приложению читать файл.

            try {
                startActivity(this)
            } catch (e: Exception) {
                Toast.makeText(this@EditActivity, "Не удалось открыть PDF файл: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickShareProject(view: View) {
        val title = bng.edTitle.text.toString().trim()
        val description = bng.edDescription.text.toString().trim()

        if (title.isNotEmpty() && description.isNotEmpty()) {
            // Объединяем заголовок и описание в одно сообщение
            val shareText = "$title\n\n$description"

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText) // Отправляем объединенный текст
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться проектом через"))
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }
}