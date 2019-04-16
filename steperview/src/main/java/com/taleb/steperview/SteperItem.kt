package com.taleb.steperview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.lang.Exception

class SteperItem :LinearLayout {

    private lateinit var itemSteperImageView:ImageView
    private lateinit var itemSteperTextView:TextView
    private var normalTextSize:Float = 14.0f
    private var selectedTextSize:Float = 16.0f

    constructor(context: Context,steperImageSrc: Int, steperText:String,typeface: Typeface?):super(context){
        init(context,null)
        itemSteperImageView.setImageResource(steperImageSrc)
        itemSteperTextView.text = steperText
        try {itemSteperTextView.typeface = typeface}catch (e: Exception){e.printStackTrace()}
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
        this.setPadding(8,8,8,8)
        itemSteperImageView = ImageView(context)
        val imgLP = LayoutParams(resources.getDimension(R.dimen.small_button).toInt(),resources.getDimension(R.dimen.small_button).toInt())
        itemSteperImageView.layoutParams = imgLP
        this.addView(itemSteperImageView)
        //
        itemSteperTextView = TextView(context)
        itemSteperTextView.setTextColor(Color.BLACK)
        itemSteperTextView.textSize = normalTextSize
        val txtLP = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        itemSteperTextView.layoutParams = txtLP
        this.addView(itemSteperTextView)

        //
        if (attributeSet == null){return}
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SteperView)
        try {
            val text = typedArray.getString(R.styleable.SteperView_sv_item_text)
            itemSteperTextView.text = text
            val imgSrc = typedArray.getResourceId(R.styleable.SteperView_sv_item_img_src,R.drawable.icon_not_found)
            itemSteperImageView.setImageResource(imgSrc)
            try {
                val fontSrc = typedArray.getString(R.styleable.SteperView_sv_font)
                val typeface = Typeface.createFromAsset(context.assets, fontSrc)
                itemSteperTextView.typeface = typeface
            }catch (e: Exception){e.printStackTrace()}

        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    fun select(isSelected : Boolean) {
        if (isSelected) {
            val imgLP = LayoutParams(resources.getDimension(R.dimen.normal_button).toInt(),resources.getDimension(R.dimen.normal_button).toInt())
            itemSteperImageView.layoutParams = imgLP
            itemSteperTextView.textSize = selectedTextSize
        }else {
            val imgLP = LayoutParams(resources.getDimension(R.dimen.small_button).toInt(),resources.getDimension(R.dimen.small_button).toInt())
            itemSteperImageView.layoutParams = imgLP
            itemSteperTextView.textSize = normalTextSize
        }
    }

    fun setTintColor(color: Int) {
        this.itemSteperImageView.setColorFilter(color)
        this.itemSteperTextView.setTextColor(color)
    }
}