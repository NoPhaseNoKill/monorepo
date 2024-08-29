package com.nophasenokill.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val PaleNight = HexColour("#1F1B2A")
val HackerThemePink = HexColour("#C33B8F")
val BrightLavender = HexColour("#C8B4EE")
val VibrantPink = HexColour("#E773AD")

val BabyBlue = HexColour("#b1baf4")
val LightDarkBlue = HexColour("#23202D")
val PimpJacketPurple = HexColour("#7B4B94")

val Cyan = HexColour("#207440")
val Green = HexColour("#b1f2a7")

val Yellow = HexColour("#ebde76")

val BabyBlueGrey = HexColour("#A49AC2")
val LightGrey = HexColour("#42424D")

val HackerWhite = HexColour("#eee9fc")
val Orange = HexColour("D89866")

interface ColorPalette {
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

fun lightColorPalette(): ColorPalette = object : ColorPalette {

    //Lights
    override val primary: Color = HexColour("A8A8FF")
    override val basic: Color = BrightLavender
    override val disable: Color = BabyBlue
    override val text: Color = VibrantPink
    override val link: Color = Cyan

    override val error: Color = Orange
    override val warning: Color = Yellow

    //Opposites
    override val background: Color = LightDarkBlue

    override val textReverse: Color = HexColour("2E263B")
    override val success: Color = Green



    override val materialColors: Colors = lightColors(
        surface = LightGrey,
        secondary = LightDarkBlue,
        onSecondary = LightDarkBlue,
        primaryVariant = PimpJacketPurple,


        onSurface = LightDarkBlue,
        onBackground = Green // Use a light-ish color for text to stand out

    )
}

// TODO Eventually add a dark theme
fun darkColorPalette(): ColorPalette = object : ColorPalette {
    override val primary: Color = PaleNight
    override val background: Color = PaleNight
    override val basic: Color = PaleNight
    override val disable: Color = PaleNight
    override val text: Color = PaleNight
    override val textReverse: Color = PaleNight
    override val success: Color = PaleNight
    override val link: Color = PaleNight
    override val warning: Color = PaleNight
    override val error: Color = PaleNight


    override val materialColors: Colors = darkColors(

    )
}
