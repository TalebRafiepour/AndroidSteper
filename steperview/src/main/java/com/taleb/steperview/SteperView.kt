package com.taleb.steperview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.LinearLayout
import java.lang.Exception

class SteperView : LinearLayout, View.OnClickListener {

    private var typeface: Typeface? = null
    private var selectedColor: Int = Color.BLACK
    private var defaultColor: Int = Color.LTGRAY
    private var itemThumbsRes: Int? = null
    private var itemTitles: Int? = null
    private var itemSelectedSize: Int = -1
    private var itemDefaultSize: Int = -1
    private var selectedTextSize: Float = -1.0f
    private var defaultTextSize: Float = -1.0f
    private var selectedItem = 0
    private var thumbCount = 0
    //
    private var lineWidth = 0
    private var lineHeight = 1
    var listener: ISteperView? = null
    //
    private lateinit var changeBound: ChangeBounds

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attributeSet: AttributeSet?) {
        this.changeBound = ChangeBounds()
        this.changeBound.startDelay = 0
        this.changeBound.interpolator = AnticipateOvershootInterpolator()
        this.changeBound.duration = 500
        this.gravity = Gravity.CENTER
        //
        if (attributeSet == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SteperView)
        try {
            this.selectedColor = typedArray.getColor(R.styleable.SteperView_sv_item_selected_color, Color.BLACK)
            this.defaultColor = typedArray.getColor(R.styleable.SteperView_sv_item_default_color, Color.LTGRAY)
            this.itemThumbsRes = typedArray.getResourceId(R.styleable.SteperView_sv_item_thumbs, 0)
            this.itemTitles = typedArray.getResourceId(R.styleable.SteperView_sv_item_titles, 0)
            this.selectedItem = typedArray.getInt(R.styleable.SteperView_sv_selected_position, 0)
            this.itemSelectedSize = typedArray.getDimensionPixelOffset(R.styleable.SteperView_sv_item_selected_size, -1)
            this.itemDefaultSize = typedArray.getDimensionPixelOffset(R.styleable.SteperView_sv_item_default_size, -1)
            this.defaultTextSize = typedArray.getDimension(R.styleable.SteperView_sv_default_text_size, -1.0f)
            this.selectedTextSize = typedArray.getDimension(R.styleable.SteperView_sv_selected_text_size, -1.0f)
            try {
                val fontSrc = typedArray.getString(R.styleable.SteperView_sv_font)
                val typeface = Typeface.createFromAsset(context.assets, fontSrc)
                this.typeface = typeface
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            typedArray.recycle()
        }

        initUI(context)
    }


    private fun initUI(context: Context) {
        if (itemThumbsRes == null && itemTitles == null) return
        if (itemThumbsRes == 0 && itemTitles == 0) return

        val resourceArray = resources.obtainTypedArray(itemThumbsRes!!)
        val titleArray = resources.obtainTypedArray(itemTitles!!)

        this.thumbCount = resourceArray.length()
        for (i in 0 until resourceArray.length()) {
            val resId = resourceArray.getResourceId(i, 0)
            val title = titleArray.getText(i)

            val steperItem = SteperItem(context, resId, "$title", typeface)
            val param = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            steperItem.layoutParams = param
            steperItem.id = i
            steperItem.defaultSize = this.itemDefaultSize
            steperItem.selectedSize = this.itemSelectedSize
            steperItem.selectedTextSize = this.selectedTextSize
            steperItem.defaultTextSize = this.defaultTextSize

            steperItem.setTintColor(defaultColor)
            steperItem.setOnClickListener(this)
            this.addView(steperItem)
            if (i != resourceArray.length() - 1) {
                val lineView = View(context)
                if (this.orientation == VERTICAL) {
                    lineHeight = 0
                    lineWidth = 1
                } else {
                    lineHeight = 1
                    lineWidth = 0
                }
                val param1 = LinearLayout.LayoutParams(lineWidth, lineHeight, 1.0f)
                lineView.layoutParams = param1
                lineView.setBackgroundColor(defaultColor)
                this.addView(lineView)
            }
        }
        resourceArray.recycle()
        titleArray.recycle()

        selectItem(this.selectedItem)
    }

    override fun onClick(v: View?) {
        if (v != null && v is SteperItem) {
            if (v.id == this.selectedItem) {return}
            selectItem(v.id)
        }
    }

    fun disableItem(position: Int) {
        findViewById<SteperItem>(position).isEnabled = false
    }

    fun enableItem(position: Int) {
        findViewById<SteperItem>(position).isEnabled = true
    }

    fun gotToNext() {
        if (this.selectedItem + 1 >= thumbCount) {
            return
        }
        if (findViewById<SteperItem>(this.selectedItem + 1).isEnabled) {
            this.selectedItem += 1
            selectItem(this.selectedItem)
        }
    }

    fun gotToPrev() {
        if (this.selectedItem - 1 < 0) {
            return
        }
        if (findViewById<SteperItem>(this.selectedItem - 1).isEnabled) {
            this.selectedItem -= 1
            selectItem(this.selectedItem)
        }
    }

    fun selectItem(index: Int) {
        this.selectedItem = index
        listener?.onStepItemClick(index,this)
        TransitionManager.beginDelayedTransition(this, changeBound)
        var temp = -1
        var i = 0
        while (i < childCount) {
            if (getChildAt(i) is SteperItem) {
                temp += 1
                if (temp < index) {
                    (getChildAt(i) as SteperItem).setTintColor(selectedColor)
                    (getChildAt(i) as SteperItem).select(false)
                    if (i - 1 > 0) {
                        getChildAt(i - 1).setBackgroundColor(selectedColor)
                        if (temp == index) {
                            getChildAt(i - 1).layoutParams = LayoutParams(lineWidth, lineHeight, 4.0f)
                        } else {
                            getChildAt(i - 1).layoutParams = LayoutParams(lineWidth, lineHeight, 1.0f)
                        }
                        i += 1
                    }
                } else if (temp == index) {
                    (getChildAt(i) as SteperItem).setTintColor(selectedColor)
                    (getChildAt(i) as SteperItem).select(true)
                    if (i - 1 > 0) {
                        getChildAt(i - 1).setBackgroundColor(selectedColor)
                        if (temp == index) {
                            getChildAt(i - 1).layoutParams = LayoutParams(lineWidth, lineHeight, 4.0f)
                        } else {
                            getChildAt(i - 1).layoutParams = LayoutParams(lineWidth, lineHeight, 1.0f)
                        }
                        i += 1
                    }
                } else {
                    (getChildAt(i) as SteperItem).setTintColor(defaultColor)
                    (getChildAt(i) as SteperItem).select(false)
                    if (i - 1 > 0) {
                        getChildAt(i - 1).setBackgroundColor(defaultColor)
                        getChildAt(i - 1).layoutParams = LayoutParams(lineWidth, lineHeight, 1.0f)
                        i += 1
                    }
                }
            }
            i += 1
        }

    }

    interface ISteperView {
        fun onStepItemClick(position: Int,view: View)
    }
}