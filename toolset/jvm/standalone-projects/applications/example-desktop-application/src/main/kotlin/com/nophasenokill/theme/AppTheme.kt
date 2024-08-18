package com.nophasenokill.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import com.nophasenokill.legacy.theme.AppTypography

// val Background = Color(0xFF282433)
// val Foreground = Color(0xFFeee9fc)
// val Selection = Color(0xFF3f3951)
// val Comment = Color(0xFF938aad)
// val Red = Color(0xFFe965a5)
// val Green = Color(0xFFb1f2a7)
// val Yellow = Color(0xFFebde76)
// val Blue = Color(0xFFb1baf4)
// val Purple = Color(0xFFe192ef)
// val Cyan = Color(0xFFb3f4f3)



/*
    This uses the hard hacker theme from: https://github.com/hardhackerlabs/themes

    The usage references for things displayed inside intellij are:

     Primary color: red
     String: green
     Number: yellow
     Method: red
     Keyword: blue
     Operator: foreground
     Class: cyan
     Variable: purple

 */


val PaleNight = HexColor("#1F1B2A")
val SoftPink = HexColor("#C33B8F")

val Background = HexColor("#211e2a")
val SecondaryBackground = HexColor("#282433")
val Foreground = HexColor("#eee9fc")
val Selection = HexColor("#3f3951")
val Comment = HexColor("#938aad")
val Red = HexColor("#e965a5")
val Green = HexColor("#b1f2a7")
val Yellow = HexColor("#ebde76")
val Blue = HexColor("#b1baf4")
val Purple = HexColor("#e192ef")
val Cyan = HexColor("#b3f4f3")



// "colors": {


// },
// "Actions.Blue": "#b1baf4",
// "Actions.Green": "#b1f2a7",
// "Actions.Grey": "#938aad",
// "Actions.GreyInline.Dark": "#b1baf4",
// "Actions.GreyInline": "#b1baf4",
// "Actions.Red": "#e965a5",
// "Actions.Yellow": "#ebde76",
// "Objects.Blue": "#b1baf4",
// "Objects.Green": "#b1f2a7",
// "Objects.GreenAndroid": "#b1f2a7",
// "Objects.Grey": "#938aad",
// "Objects.Pink": "#e965a5",
// "Objects.Purple": "#e192ef",
// "Objects.Red": "#e965a5",
// "Objects.RedStatus": "#ff6261",
// "Objects.Yellow": "#ebde76",
// "Objects.YellowDark": "#ebde76",
// "Objects.BlackText": "#282433",
// "Checkbox.Foreground.Selected.Dark": "foreground",
// "Checkbox.Border.Default.Dark": "accentColor",
// "Checkbox.Border.Selected.Dark": "accentColor",
// "Checkbox.Border.Disabled.Dark": "disabledColor",
// "Checkbox.Background.Default.Dark": "secondaryBackground",
// "Checkbox.Background.Disabled.Dark": "background",
// "Checkbox.Focus.Wide.Dark": "accentColor",
// "Checkbox.Focus.Thin.Selected.Dark": "accentColor",
// "Checkbox.Focus.Thin.Default.Dark": "accentColor"
// }

data class HardHackerColor(
    val hex: String,
    val name: String,
) {
    val color: Color = HexColor(hex)
}

// #987CB0 - VIolet
// #A85E99 - Light Pink
// #AE4F82 - Magenta
// #858393 - Concrete Grey
// #31324F - Ship Grey
// #373246 - Palenight Blue
// #2F2B3B - Spacey
// #2C2837 - Raisin black1
// #282533 - Raisin black2


val ColorsAccentColor = HardHackerColor(hex = "#e965a5", name ="ColorsAccentColor")
val ColorsForeground = HardHackerColor(hex = "#eee9fc", name ="ColorsForeground")
val ColorsBackground = HardHackerColor(hex = "#1a1821", name ="ColorsBackground")
val ColorsSecondaryBackground = HardHackerColor(hex = "#211e2a", name ="ColorsSecondaryBackground")
val ColorsHoverBackground = HardHackerColor(hex = "#2a2636", name ="ColorsHoverBackground")
val ColorsSelectionBackground = HardHackerColor(hex = "#383348", name ="ColorsSelectionBackground")
val ColorsBorderColor = HardHackerColor(hex = "#383348", name ="ColorsBorderColor")
val ColorsDisabledColor = HardHackerColor(hex = "#383348", name ="ColorsDisabledColor")
val ColorsErrorColor = HardHackerColor(hex = "#ff6261", name ="ColorsErrorColor")

val HARD_HACKER_COLORS = listOf(
    ColorsAccentColor,
    ColorsForeground,
    ColorsBackground,
    ColorsSecondaryBackground,
    ColorsHoverBackground,
    ColorsSelectionBackground,
    ColorsBorderColor,
    ColorsDisabledColor,
    ColorsErrorColor,
)

private val DarkColorScheme = darkColorScheme(
    onPrimary = Foreground,
    secondary = SoftPink,
    // onSecondary = Foreground,
    // tertiary = Cyan,
    // onTertiary = Foreground,
    // error = Red,
    // onError = Foreground,
    // background = Background,
    // onBackground = Foreground,
    // surface = SecondaryBackground,
    // onSurface = Foreground,
)

private val LightColorScheme = lightColorScheme(
    onPrimary = Foreground,
    secondary = SoftPink,
    // onSecondary = Foreground,
    // tertiary = Cyan,
    // onTertiary = Foreground,
    // error = Red,
    // onError = Foreground,
    // background = Background,
    // onBackground = Foreground,
    // surface = SecondaryBackground,
    // onSurface = Foreground,
)


@Composable
fun AppTheme(
    themeToggleValueLegacy: ToggleableState = ToggleableState.Indeterminate,
    content: @Composable () -> Unit,
) {

    val colors = when(themeToggleValueLegacy) {
        ToggleableState.On, ToggleableState.Indeterminate -> DarkColorScheme
        ToggleableState.Off -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
    ) {
        content()
    }
}

