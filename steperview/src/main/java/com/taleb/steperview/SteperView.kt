package com.taleb.steperview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import java.lang.Exception

class SteperView : LinearLayout{

    private lateinit var view:LinearLayout
    private var typeface:Typeface? = null
    private var selectedColor:Int? = null
    private var defaultColor:Int? = null
    private var itemThumbsRes:Int? = null
    private var itemTitles:Int? = null

    constructor(context: Context):super(context){
        init(context,null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context,attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context,attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attributeSet: AttributeSet?) {
        this.orientation = VERTICAL
        this.gravity = Gravity.CENTER
        //
        if (attributeSet == null){return}
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SteperView)
        try {
            this.selectedColor = typedArray.getColor(R.styleable.SteperView_sv_item_selected_color,Color.BLACK)
            this.defaultColor = typedArray.getColor(R.styleable.SteperView_sv_item_default_color,Color.LTGRAY)
            this.itemThumbsRes = typedArray.getResourceId(R.styleable.SteperView_sv_item_thumbs,0)
            this.itemTitles = typedArray.getResourceId(R.styleable.SteperView_sv_item_titles,0)
            try {
                val fontSrc = typedArray.getString(R.styleable.SteperView_sv_font)
                val typeface = Typeface.createFromAsset(context.assets, fontSrc)
                this.typeface = typeface
            }catch (e: Exception){e.printStackTrace()}

        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            typedArray.recycle()
        }

        initUI(context)
    }


    private fun initUI(context: Context) {
        if (itemThumbsRes == null || itemTitles == null) return
        val resourceArray = resources.obtainTypedArray(itemThumbsRes!!)
        val titleArray = resources.obtainTypedArray(itemTitles!!)
        if (resourceArray.length() != titleArray.length()) {return}

        for (i in 0 until resourceArray.length()) {
            val resId = resourceArray.getResourceId(i,0)
            val title = titleArray.getText(i)

            val steperItem = SteperItem(context,resId,"$title",typeface)
            val param = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
            steperItem.layoutParams = param
            this.addView(steperItem)
            if (i != resourceArray.length()-1) {
                val lineView = View(context)
                var weight = 1.0f
                if (i == 0){weight = (resourceArray.length()-1).toFloat()}
                val param1 = LinearLayout.LayoutParams(1,0,weight)
                lineView.layoutParams = param1
                lineView.setBackgroundColor(Color.LTGRAY)
                this.addView(lineView)
            }
        }
        resourceArray.recycle()
        titleArray.recycle()
    }
}