package com.naim.swiftnotes.presentation.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.naim.swiftnotes.presentation.components.defaultScreenEnterAnimation
import com.naim.swiftnotes.presentation.components.defaultScreenExitAnimation
import com.naim.swiftnotes.presentation.components.slideScreenEnterAnimation
import com.naim.swiftnotes.presentation.components.slideScreenExitAnimation

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = { defaultScreenEnterAnimation() },
    exitTransition = { defaultScreenExitAnimation() },
    popEnterTransition = { defaultScreenEnterAnimation() },
    popExitTransition = { defaultScreenExitAnimation() },
    content = content
)

fun NavGraphBuilder.slideInComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = { slideScreenEnterAnimation() },
    exitTransition = { defaultScreenExitAnimation() },
    popEnterTransition = { defaultScreenEnterAnimation() },
    popExitTransition = { slideScreenExitAnimation() },
    content = content
)