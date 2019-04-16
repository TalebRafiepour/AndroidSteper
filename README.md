# AndroidSteper
simple steper view for android

1.setup 

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  -------------------------------------
  
  dependencies {
	        implementation 'com.github.TalebRafiepour:AndroidSteper:0.1'
	}
  
 ----------------------------------------
 
 2.use
 
```
<com.taleb.steperview.SteperView
        android:id="@+id/mainSteper"
        android:layout_margin="12dp"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="center|start"
        app:sv_item_thumbs="@array/thumbs"
        app:sv_item_titles="@array/titles"
        app:sv_font="fonts/IRAN Sans.ttf"
        app:sv_selected_position="0"/>
	```
