package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.ui.theme.title1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddCompanyScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    userInfo: UserInfo,
    navController: NavController,
    confirmBtnOnClick: (String) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var leaderName by remember { mutableStateOf("") }

    var errorDialogOpen by remember { mutableStateOf(false) }
    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    ) else if (jwtExpiredDialogOpen) {
        BackToLoginDialog(sendEvent = sendEvent)
    }

    LaunchedEffect(userInfo) {
        leaderName = userInfo.userName

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                    launchSingleTop = event.launchOnSingleTop
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                } else if (event.dialog == DialogState.JwtExpired) {
                    jwtExpiredDialogOpen = event.action == DialogAction.Open
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.register),
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.add_company),
                    style = Typography.title1
                )

                Spacer(modifier = Modifier.height(30.dp))

                Label(text = stringResource(id = R.string.company_name))

                Spacer(modifier = Modifier.height(8.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = companyName,
                    hint = stringResource(id = R.string.company_name_hint),
                    onValueChange = { companyName = it })
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.compnay_name_info),
                    style = Typography.body4,
                    color = Font70747E
                )

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.leader_name))

                Spacer(modifier = Modifier.height(8.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = leaderName,
                    enabled = false,
                    readOnly = true
                )

                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Column {
                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = { confirmBtnOnClick(companyName) }
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}