package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.databinding.FragmentClassifiedRichTextEditorBinding
import com.chinalwb.are.AREditText
import com.chinalwb.are.styles.toolbar.IARE_Toolbar
import com.chinalwb.are.styles.toolitems.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ClassifiedRichTextEditor : BottomSheetDialogFragment() {
    var binding: FragmentClassifiedRichTextEditorBinding? = null

    private var mToolbar: IARE_Toolbar? = null

    private var mEditText: AREditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClassifiedRichTextEditorBinding.inflate(inflater, container, false)

        binding?.apply {
            toolbar()

        }
        return binding?.root
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

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}