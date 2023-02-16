package com.hana.umuljeong.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.theme.Shapes

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.setting),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(13.dp))

            SettingItem(
                onClick = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_category),
                title = stringResource(id = R.string.change_category)
            )

            SettingItem(
                onClick = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_password),
                title = stringResource(id = R.string.change_password)
            )

            SettingItem(
                onClick = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_logout),
                title = stringResource(id = R.string.logout)
            )

            SettingItem(
                onClick = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_exit),
                title = stringResource(id = R.string.exit_company)
            )

            SettingItem(
                onClick = { /*TODO*/ },
                icon = painterResource(id = R.drawable.ic_app_info),
                title = stringResource(id = R.string.app_info)
            )
        }
    }
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    icon: Painter,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 17.dp, bottom = 17.dp, start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = icon,
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(text = title)
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}