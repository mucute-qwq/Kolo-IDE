package io.github.mucute.qwq.koloide.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Code
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Javascript
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.githun.mucute.qwq.koloide.R
import io.githun.mucute.qwq.koloide.component.GalleryCard
import io.githun.mucute.qwq.koloide.navigation.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen() {
    val navController = _root_ide_package_.io.github.mucute.qwq.koloide.navigation.LocalNavController.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.new_project))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.TwoTone.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(4) { item ->
                    _root_ide_package_.io.github.mucute.qwq.koloide.component.GalleryCard(
                        rememberVectorPainter(Icons.TwoTone.Code),
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}