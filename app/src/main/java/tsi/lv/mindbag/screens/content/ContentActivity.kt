package tsi.lv.mindbag.screens.content

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.header_note.*
import tsi.lv.mindbag.*
import tsi.lv.mindbag.model.Model
import tsi.lv.mindbag.model.domain.Note
import javax.inject.Inject

class ContentActivity : AppCompatActivity() {

    var mEditModeOn = false

    @Inject
    lateinit var model : Model

    var note : Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.injector.inject(this)
        setContentView(R.layout.activity_note)

        setSupportActionBar(noteToolbar)

        supportActionBar?.title = "Note details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        note = model.getNote(intent.extras.getInt(EXTRA_ACTIVITY_DISPLAYED_NOTE_ID))
        noteItemContent.setText(note?.content)
        noteItemCaption.setText(note?.caption)

        noteToolbar.setNavigationOnClickListener { onUpOrCheckButtonClick() }
        noteEditFab.setOnClickListener { onEditButtonClick() }
    }

    fun onEditButtonClick() {
        toggleEditMode()
    }

    fun onUpOrCheckButtonClick() {
        if (mEditModeOn) {
            toggleReadMode()
        }
        else navigateUp()
    }

    fun toggleEditMode() {
        mEditModeOn = true
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_check)
        noteEditFab.hide()

        noteItemContent.isFocusableInTouchMode = true
        noteItemContent.isLongClickable = true

        showKeyboard(this, noteItemContent)
    }

    fun toggleReadMode() {
        mEditModeOn = false
        supportActionBar?.setHomeAsUpIndicator(null)
        noteEditFab.show()

        noteItemContent.isFocusable = false
        noteItemContent.isLongClickable = false
        noteItemContent.clearFocus()

        hideKeyboard(this, noteItemContent)

        save()
    }

    fun navigateUp() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        save()
        super.onBackPressed()
    }

    fun save() {
        if (this.note != null) {
            this.note?.caption = noteItemCaption.text.toString()
            this.note?.content = noteItemContent.text.toString()
            model.updateNote(note!!)
        }
    }
}
