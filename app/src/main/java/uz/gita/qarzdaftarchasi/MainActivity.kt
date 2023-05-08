package uz.gita.qarzdaftarchasi

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.qarzdaftarchasi.lang.LangType
import uz.gita.qarzdaftarchasi.lang.Word
import uz.gita.qarzdaftarchasi.navigation.NavigationHandler
import uz.gita.qarzdaftarchasi.presentation.main.MainScreen
import uz.gita.qarzdaftarchasi.shp.Shp
import uz.gita.qarzdaftarchasi.ui.theme.QarzDaftarchasiTheme
import uz.gita.qarzdaftarchasi.utils.Const
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    @Inject
    lateinit var shp: Shp

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Word.ChangeLang(langType = if (shp.getBool(Const.isLatin)) LangType.LATIN else LangType.KRILL)
            QarzDaftarchasiTheme() {
                Navigator(screen = MainScreen(), onBackPressed = {
                    true
                }) { navigator ->
                    navigationHandler.navStack.onEach {
                        it.invoke(navigator)
                    }.launchIn(lifecycleScope)
                    SlideTransition(navigator)
                }
            }
        }
    }
}

