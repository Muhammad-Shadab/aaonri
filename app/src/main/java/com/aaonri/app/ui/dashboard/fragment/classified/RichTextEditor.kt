package com.aaonri.app.ui.dashboard.fragment.classified

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.databinding.ActivityRichTextEditorBinding
import com.aaonri.app.ui.dashboard.fragment.event.post_event.PostEventBasicDetailsFragment
import com.aaonri.app.utils.PreferenceManager
import com.chinalwb.are.AREditText
import com.chinalwb.are.styles.toolbar.IARE_Toolbar
import com.chinalwb.are.styles.toolitems.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RichTextEditor : AppCompatActivity() {
    var binding: ActivityRichTextEditorBinding? = null
    private var mToolbar: IARE_Toolbar? = null

    private var mEditText: AREditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRichTextEditorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        supportActionBar?.hide()
//        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        binding?.apply {
            binding?.arEditText?.text = Html.fromHtml(PreferenceManager<String>(applicationContext)["description", ""]) as Editable?

            navigateBack.setOnClickListener {
                finish()
            }

            toolbar()
            val richtext :PostEventBasicDetailsFragment
            doneTextTv.setOnClickListener {

                PreferenceManager<String>(applicationContext)["description"] =
                    arEditText.html.toString()

                finish()
            }
        }

    }

    private fun toolbar() {
        mToolbar = binding?.areToolbar
        val bold: IARE_ToolItem = ARE_ToolItem_Bold()
        val italic: IARE_ToolItem = ARE_ToolItem_Italic()
        val underline: IARE_ToolItem = ARE_ToolItem_Underline()
        val strikethrough: IARE_ToolItem = ARE_ToolItem_Strikethrough()
        val quote: IARE_ToolItem = ARE_ToolItem_Quote()
        val listNumber: IARE_ToolItem = ARE_ToolItem_ListNumber()
        val listBullet: IARE_ToolItem = ARE_ToolItem_ListBullet()
        val hr: IARE_ToolItem = ARE_ToolItem_Hr()
        val link: IARE_ToolItem = ARE_ToolItem_Link()
        val subscript: IARE_ToolItem = ARE_ToolItem_Subscript()
        val superscript: IARE_ToolItem = ARE_ToolItem_Superscript()
        val left: IARE_ToolItem = ARE_ToolItem_AlignmentLeft()
        val center: IARE_ToolItem = ARE_ToolItem_AlignmentCenter()
        val right: IARE_ToolItem = ARE_ToolItem_AlignmentRight()
        val image: IARE_ToolItem = ARE_ToolItem_Image()
        val video: IARE_ToolItem = ARE_ToolItem_Video()
        val at: IARE_ToolItem = ARE_ToolItem_At()
        val fontColor: IARE_ToolItem = ARE_ToolItem_FontColor()
        val backgroundColor: IARE_ToolItem = ARE_ToolItem_BackgroundColor()
        mToolbar?.addToolbarItem(bold)
        mToolbar?.addToolbarItem(italic)
        mToolbar?.addToolbarItem(underline)
        mToolbar?.addToolbarItem(strikethrough)
        mToolbar?.addToolbarItem(quote)
        mToolbar?.addToolbarItem(listNumber)
        mToolbar?.addToolbarItem(listBullet)
        mToolbar?.addToolbarItem(hr)
        mToolbar?.addToolbarItem(link)
        mToolbar?.addToolbarItem(subscript)
        mToolbar?.addToolbarItem(superscript)
        mToolbar?.addToolbarItem(left)
        mToolbar?.addToolbarItem(center)
        mToolbar?.addToolbarItem(right)
        mToolbar?.addToolbarItem(image)
        mToolbar?.addToolbarItem(video)
        mToolbar?.addToolbarItem(at)
        mToolbar?.addToolbarItem(fontColor);
        mToolbar?.addToolbarItem(backgroundColor);
        binding?.arEditText?.setToolbar(mToolbar)


//
        initToolbarArrow()
    }

    private fun initToolbarArrow() {

    }


}