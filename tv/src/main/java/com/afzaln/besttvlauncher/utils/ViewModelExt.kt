package com.afzaln.besttvlauncher.utils

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.afzaln.besttvlauncher.core.Locator
import java.lang.reflect.InvocationTargetException

inline fun <reified VM : ViewModel> Fragment.locatorViewModel(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
) = ViewModelLazy(
    VM::class,
    storeProducer = { ownerProducer().viewModelStore },
    factoryProducer = { LocatorViewModelFactory(Locator) }
)

inline fun <reified VM : ViewModel> ComponentActivity.locatorViewModel() = ViewModelLazy(
    VM::class,
    storeProducer = { viewModelStore },
    factoryProducer = { LocatorViewModelFactory(Locator) }
)

@Composable
inline fun <reified VM : ViewModel> locatorViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    factory: ViewModelProvider.Factory? = LocatorViewModelFactory(Locator)
): VM = androidx.lifecycle.viewmodel.compose.viewModel(
    VM::class.java,
    viewModelStoreOwner,
    key,
    factory
)

open class LocatorViewModelFactory(
    private val locator: Locator,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            // Constructors of ViewModel subclasses using this factory should also be excluded from
            // obfuscation in proguard.pro.
            val constructor = modelClass.constructors.first()
            val args = constructor.parameterTypes.map {
                locator.provide(it)
            }

            @Suppress("UNCHECKED_CAST")
            constructor.newInstance(*args.toTypedArray()) as T
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}