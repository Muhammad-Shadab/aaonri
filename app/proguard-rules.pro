# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response


# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault
-keepnames class androidx.navigation.fragment.NavHostFragment

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


# Application classes that will be serialized/deserialized over Gson, keepclassmembers
-keepattributes InnerClasses
-keep class com.aaonri.app.$ { *; }
-keep class com.aaonri.app.data.advertise.model.** { *; }
-keepclassmembers class com.aaonri.app.data.advertise.model.** { *; }

-keep class com.aaonri.app.data.authentication.login.model.** { *; }
-keepclassmembers class com.aaonri.app.data.authentication.login.model.** { *; }

-keep class com.aaonri.app.data.authentication.register.model.** { *; }
-keepclassmembers class com.aaonri.app.data.authentication.register.model.** { *; }

-keep class com.aaonri.app.data.classified.model.** { *; }
-keepclassmembers class com.aaonri.app.data.classified.model.** { *; }

-keep class com.aaonri.app.data.event.model.** { *; }
-keepclassmembers class com.aaonri.app.data.event.model.** { *; }

-keep class com.aaonri.app.data.home.model.** { *; }
-keepclassmembers class com.aaonri.app.data.home.model.** { *; }

-keep class com.aaonri.app.data.immigration.model.** { *; }
-keepclassmembers class com.aaonri.app.data.immigration.model.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  * rewind();
}