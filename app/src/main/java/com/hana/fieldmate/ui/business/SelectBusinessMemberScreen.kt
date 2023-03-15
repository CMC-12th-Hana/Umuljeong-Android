package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.viewmodel.BusinessUiState
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FSearchTextField
import com.hana.fieldmate.ui.theme.BgF8F8FA
import com.hana.fieldmate.ui.theme.Shapes
import com.hana.fieldmate.ui.theme.Typography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SelectBusinessMemberScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessUiState,
    userInfo: UserInfo,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusiness: () -> Unit,
    loadCompanyMembers: (Long, String?) -> Unit,
    selectedMemberList: List<MemberNameEntity>,
    updateMembers: () -> Unit,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
    navController: NavController
) {
    val memberEntityList: List<MemberNameEntity> = uiState.memberNameList

    var memberName by rememberSaveable { mutableStateOf("") }

    var selectedName by rememberSaveable { mutableStateOf("") }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(selectedName) {
        loadCompanyMembers(userInfo.companyId, selectedName)
    }

    LaunchedEffect(true) {
        loadBusiness()
        loadCompanyMembers(userInfo.companyId, null)

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.add_members),
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
            LazyColumn(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    FSearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = memberName,
                        hint = stringResource(id = R.string.search_member_hint),
                        onSearch = { selectedName = it },
                        onValueChange = { memberName = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(memberEntityList) { member ->
                    SelectableMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        memberEntity = member,
                        selected = selectedMemberList.contains(member),
                        selectMember = selectMember,
                        unselectMember = unselectMember
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Column {
                Spacer(Modifier.height(40.dp))

                FButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.complete),
                    onClick = { updateMembers() }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableMemberItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    memberEntity: MemberNameEntity,
    selected: Boolean,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.ic_member_profile),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = memberEntity.name, style = Typography.body2)
            }

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(onClick = {
                    if (selected) unselectMember(memberEntity) else selectMember(
                        memberEntity
                    )
                }) {
                    Icon(
                        painter = painterResource(id = if (selected) R.drawable.ic_circle_remove else R.drawable.ic_circle_add),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}