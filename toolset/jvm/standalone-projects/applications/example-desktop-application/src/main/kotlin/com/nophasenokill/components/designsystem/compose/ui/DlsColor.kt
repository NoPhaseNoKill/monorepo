package com.nophasenokill.components.designsystem.compose.ui

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.nophasenokill.theme.*


val PaleNight = HexColor("#1F1B2A")
val HackerThemePink = HexColor("#C33B8F")
val BrightLavender = HexColor("#C8B4EE")
val VibrantPink = HexColor("#E773AD")

val BabyBlue = HexColor("#b1baf4")
val LightDarkBlue = HexColor("#23202D")
val PimpJacketPurple = HexColor("#7B4B94")

val Cyan = HexColor("#207440")
val Green = HexColor("#b1f2a7")

val Yellow = HexColor("#ebde76")

val BabyBlueGrey = HexColor("#A49AC2")
val LightGrey = HexColor("#42424D")

val HackerWhite = HexColor("#eee9fc")
val Orange = HexColor("D89866")





//Default dark mode
// object DlsColors {
//     // val primary = PaleNight
//     // val background = SecondaryBackground
//     // val backgroundReverse = PaleNight
//     // val basic = Blue
//     // val disable = SoftPink.copy(alpha = 0.24f)
//     // val text = Blue
//     // val textReverse = Blue
//     // val success = Green
//     // val link = Cyan
//     // val warning = Yellow
//     // val error = Red
//     val primary = BabyBlueGrey
//     val background = Gray
//     val backgroundReverse = Yellow
//     val basic = Yellow
//     val disable = Yellow
//     val text = Yellow
//     val textReverse = Yellow
//     val success = Yellow
//     val warning = Yellow
//     val error = Yellow
//     val link = Yellow
// }

interface DlsColorPalette {
    val primary: Color
    val background: Color
    val basic: Color
    val disable: Color
    val text: Color
    val textReverse: Color
    val success: Color
    val link: Color
    val warning: Color
    val error: Color

    val materialColors: Colors
}

fun dlsLightColorPalette(): DlsColorPalette = object : DlsColorPalette {

    //Lights
    override val primary: Color = HexColor("A8A8FF")
    override val basic: Color = BrightLavender
    override val disable: Color = BabyBlue
    override val text: Color = VibrantPink
    override val link: Color = Cyan


    override val error: Color = Orange
    override val warning: Color = Yellow

    //Opposites
    override val background: Color = LightDarkBlue

    override val textReverse: Color = HexColor("2E263B")
    override val success: Color = Green



    override val materialColors: Colors = lightColors(



        // onPrimary = LightDarkBlue,
        // primary = LightDarkBlue,
        //
        // background = DarkBlue,
        //
        surface = LightGrey,
        //
        secondary = LightDarkBlue,
        onSecondary = LightDarkBlue,
        primaryVariant = PimpJacketPurple,


        onSurface = LightDarkBlue,
        onBackground = Green // Use a lighter color for text to stand out

    )

    // override val primary: Color = Foreground
    // override val background: Color = Foreground
    // override val basic: Color = Foreground
    // override val disable: Color = Foreground
    // override val text: Color = Foreground
    // override val textReverse: Color = Foreground
    // override val success: Color = Foreground
    // override val link: Color = Foreground
    // override val warning: Color = Foreground
    // override val error: Color = Foreground
    //
    // override val materialColors: Colors = lightColors(
    //     // onPrimary = com.nophasenokill.theme.Foreground,
    //     // secondary = com.nophasenokill.theme.SoftPink,
    //     // primary = DlsColors.primary,
    //     // background = DlsColors.background,
    //     // surface = DlsColors.background,
    //     // onSurface = DlsColors.text
    // )
}

fun dlsDarkColorPalette(): DlsColorPalette = object : DlsColorPalette {
    override val primary: Color = PaleNight
    override val background: Color = PaleNight
    override val basic: Color = PaleNight
    override val disable: Color = PaleNight
    override val text: Color =PaleNight
    override val textReverse: Color = PaleNight
    override val success: Color = PaleNight
    override val link: Color = PaleNight
    override val warning: Color = PaleNight
    override val error: Color = PaleNight


    override val materialColors: Colors = darkColors(
        // primary = Background,
        // primaryVariant = SoftPink,
        // secondary = Foreground,
        // secondaryVariant = Selection,
        // background = Comment,
        // surface = SecondaryBackground,
        // error = Green,
        // onPrimary = Yellow,
        // onSecondary = Blue,
        // onBackground = Purple,
        // onSurface = Cyan,
        // onError = Red,
    )
}

// private val DarkColorScheme = darkColorScheme(
//     onPrimary = com.nophasenokill.theme.Foreground,
//     secondary = com.nophasenokill.theme.SoftPink,
//     // onSecondary = Foreground,
//     // tertiary = Cyan,
//     // onTertiary = Foreground,
//     // error = Red,
//     // onError = Foreground,
//     // background = Background,
//     // onBackground = Foreground,
//     // surface = SecondaryBackground,
//     // onSurface = Foreground,
// )
//
// private val LightColorScheme = lightColorScheme(
//     onPrimary = com.nophasenokill.theme.Foreground,
//     secondary = com.nophasenokill.theme.SoftPink,
//     // onSecondary = Foreground,
//     // tertiary = Cyan,
//     // onTertiary = Foreground,
//     // error = Red,
//     // onError = Foreground,
//     // background = Background,
//     // onBackground = Foreground,
//     // surface = SecondaryBackground,
//     // onSurface = Foreground,
// )
