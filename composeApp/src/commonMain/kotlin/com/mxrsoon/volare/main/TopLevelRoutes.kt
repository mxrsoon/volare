package com.mxrsoon.volare.main

import com.mxrsoon.volare.collections.CollectionsRoute
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.collections_label
import com.mxrsoon.volare.composeapp.generated.resources.home_24px
import com.mxrsoon.volare.composeapp.generated.resources.home_label
import com.mxrsoon.volare.composeapp.generated.resources.search_24px
import com.mxrsoon.volare.composeapp.generated.resources.search_label
import com.mxrsoon.volare.composeapp.generated.resources.stacks_24px
import com.mxrsoon.volare.home.HomeRoute
import com.mxrsoon.volare.search.SearchRoute
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TopLevelRoutes(
    val label: StringResource,
    val icon: DrawableResource,
    val route: Any
) {
    Home(Res.string.home_label, Res.drawable.home_24px, HomeRoute),
    Search(Res.string.search_label, Res.drawable.search_24px, SearchRoute),
    Collections(Res.string.collections_label, Res.drawable.stacks_24px, CollectionsRoute)
}