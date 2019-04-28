# AndroidSteper
simple steper view for android

1.setup 

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
  -------------------------------------
  
```
  dependencies {
	        implementation 'com.github.TalebRafiepour:AndroidSteper:0.7'
	}
   
```
 ----------------------------------------
 
 ![alt text](https://github.com/TalebRafiepour/AndroidSteper/blob/master/screen.gif)
 
 
 2.use
```
<com.taleb.steperview.SteperView
        android:id="@+id/mainSteper"
        android:layout_margin="12dp"
        android:layout_width="wrap_content"
	android:orientation="vertical"
        android:layout_height="match_parent"
        android:layout_gravity="center|start"
        app:sv_item_thumbs="@array/thumbs"
        app:sv_item_titles="@array/titles"
        app:sv_font="fonts/IRAN Sans.ttf"
        app:sv_item_default_size="@dimen/small_button"
        app:sv_item_selected_size="@dimen/normal_button"
        app:sv_item_selected_color="@color/colorPrimary"
        app:sv_item_default_color="@color/colorAccent"
        app:sv_selected_position="0"
	app:sv_default_text_size="14sp"
        app:sv_selected_text_size="16sp"/>
