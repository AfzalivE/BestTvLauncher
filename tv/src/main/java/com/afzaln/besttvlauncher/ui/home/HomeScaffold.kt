package com.afzaln.besttvlauncher.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.settings.SettingsActivity
import com.afzaln.besttvlauncher.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(content: @Composable (PaddingValues) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { TitleBar() },
                actions = {
                    IconButton(
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            context.startActivity(SettingsActivity.createIntent(context))
                        }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(
                                id = R.string.settings_title
                            )
                        )
                    }
                }
            )
        },
        content = content
    )

    SideEffect {
        // focusRequester.requestFocus()
    }
}

@Composable
fun TitleBar() {
    Text(text = stringResource(id = R.string.your_apps))
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        TitleBar()
    }
}
