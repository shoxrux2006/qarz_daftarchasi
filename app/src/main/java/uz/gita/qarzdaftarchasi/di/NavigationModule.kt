package uz.gita.qarzdaftarchasi.di
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.qarzdaftarchasi.navigation.AppNavigation
import uz.gita.qarzdaftarchasi.navigation.NavigationDispatcher
import uz.gita.qarzdaftarchasi.navigation.NavigationHandler


@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    fun appNavigator(dispatcher: NavigationDispatcher): AppNavigation

    @Binds
    fun navigationHandler(dispatcher: NavigationDispatcher): NavigationHandler
}