package com.hana.umuljeong.ui.component.imagepicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithExitBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.theme.Main356DF8


@Composable
fun ImagePickerScreen(
    modifier: Modifier = Modifier,
    maxImgCount: Int = 10,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: ImageViewModel = viewModel(
        factory = ImageViewModelFactory(
            repository = ImageRepository(context = context)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    Scaffold(
        topBar = {
            UAppBarWithExitBtn(
                title = stringResource(id = R.string.recent_photos),
                exitBtnOnClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier.padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            PickerContent(
                modifier = modifier.padding(innerPadding),
                loadImages = viewModel::loadImages,
                insertImage = viewModel::insertImage,
                images = viewModel.images,
                selectedImages = viewModel.selectedImages,
                selectImage = viewModel::selectImage,
                removeImage = viewModel::removeImage,
                maxImgCount = maxImgCount
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(40.dp))

                UButton(
                    modifier = Modifier.width(335.dp),
                    onClick = { }
                ) {
                    Text(
                        text = stringResource(id = R.string.complete)
                    )
                }

                Spacer(Modifier.height(50.dp))
            }
        }

    }
}

@Composable
internal fun PickerContent(
    modifier: Modifier = Modifier,
    images: List<ImageInfo>,
    selectedImages: List<ImageInfo>,
    loadImages: () -> Unit,
    insertImage: () -> Uri?,
    selectImage: (ImageInfo) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    maxImgCount: Int,
) {
    var cameraUri: Uri? = null

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                cameraUri?.let {
                    loadImages()
                }
            }
        }

    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.0f)
                    .clickable(
                        onClick = {
                            cameraUri = insertImage()
                            cameraLauncher.launch(cameraUri)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera_32dp),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.take_photo),
                        fontSize = 14.sp
                    )
                }
            }
        }

        items(images) { image ->
            ImageItem(
                image = image,
                selectedImages = selectedImages,
                selectImage = selectImage,
                removeImage = removeImage,
                maxImgCount = maxImgCount
            )
        }
    }
}

@Composable
internal fun ImageItem(
    image: ImageInfo,
    selectedImages: List<ImageInfo>,
    selectImage: (ImageInfo) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    maxImgCount: Int
) {
    val context = LocalContext.current
    val selected = selectedImages.any { it.id == image.id }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    if (selected) {
                        removeImage(image)
                    } else {
                        if (selectedImages.size < maxImgCount) selectImage(image)
                    }
                }
            ),
        contentAlignment = Alignment.TopEnd
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image.contentUri)
                .build(),
            modifier = Modifier
                .aspectRatio(1.0F)
                .border(width = 1.dp, color = if (selected) Main356DF8 else Color.Transparent),
            filterQuality = FilterQuality.Low,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        if (selected) {
            ImageIndicator(imgCount = selectedImages.indexOf(image) + 1)
        }
    }
}


@Composable
internal fun ImageIndicator(
    imgCount: Int
) {
    Surface(
        modifier = Modifier
            .padding(10.dp)
            .size(20.dp),
        shape = CircleShape,
        color = Main356DF8
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "$imgCount",
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}