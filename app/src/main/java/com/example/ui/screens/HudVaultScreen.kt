package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.SearchFilter
import com.example.ui.HudViewModel
import com.example.ui.components.HudBadge
import com.example.ui.components.HudButton
import com.example.ui.components.HudFrameBox
import com.example.ui.components.HudImportDialog
import com.example.ui.components.HudRecordCard
import com.example.ui.components.HudRecordDetailDialog
import com.example.ui.components.HudScanningBeam
import com.example.ui.components.HudStatsDialog
import com.example.ui.theme.HudAlert
import com.example.ui.theme.HudAmber
import com.example.ui.theme.HudBackground
import com.example.ui.theme.HudBorder
import com.example.ui.theme.HudBorderGlow
import com.example.ui.theme.HudPrimary
import com.example.ui.theme.HudSecondary
import com.example.ui.theme.HudSurface
import com.example.ui.theme.HudSurfaceElevated
import com.example.ui.theme.HudTextMuted
import com.example.ui.theme.HudTextPrimary
import com.example.ui.theme.HudTextSecondary
import com.example.ui.theme.OrbitronFontFamily
import com.example.ui.theme.ShareTechMonoFontFamily
import kotlinx.coroutines.delay

@Composable
fun HudVaultScreen(
    viewModel: HudViewModel = viewModel()
) {
    val context = LocalContext.current
    val records by viewModel.records.collectAsStateWithLifecycle()
    val totalRecords by viewModel.recordCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val importProgress by viewModel.importProgress.collectAsStateWithLifecycle()
    val isImportOpen by viewModel.isImportDialogOpen.collectAsStateWithLifecycle()
    val isStatsOpen by viewModel.isStatsDialogOpen.collectAsStateWithLifecycle()
    val selectedRecord by viewModel.selectedRecord.collectAsStateWithLifecycle()
    val flashMessage by viewModel.flashMessage.collectAsStateWithLifecycle()

    // Auto-dismiss toast flash message after 3 seconds
    LaunchedEffect(flashMessage) {
        if (flashMessage != null) {
            delay(3000)
            viewModel.clearFlashMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = HudBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Top Tactical Command Header
                HudFrameBox(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = HudBorder,
                    glowColor = HudBorderGlow,
                    cornerLength = 8.dp,
                    backgroundColor = HudSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "HUD VAULT",
                                        fontFamily = OrbitronFontFamily,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HudPrimary,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    HudBadge(
                                        text = "v2.4-SECURE",
                                        color = HudSecondary,
                                        indicatorOn = true
                                    )
                                }
                                Text(
                                    text = "OFFLINE FAST SQLITE DATA ENGINE",
                                    fontFamily = ShareTechMonoFontFamily,
                                    fontSize = 10.sp,
                                    color = HudTextMuted
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Import Button
                                IconButton(
                                    onClick = { viewModel.openImportDialog() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(HudSurfaceElevated, RoundedCornerShape(2.dp))
                                        .border(1.dp, HudSecondary.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                                        .testTag("open_import_dialog_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FileUpload,
                                        contentDescription = "Import CSV",
                                        tint = HudSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Diagnostics / Stats Button
                                IconButton(
                                    onClick = { viewModel.openStatsDialog() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(HudSurfaceElevated, RoundedCornerShape(2.dp))
                                        .border(1.dp, HudPrimary.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                                        .testTag("open_stats_dialog_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Database Diagnostics",
                                        tint = HudPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Status Telemetry Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                HudBadge(
                                    text = "TOTAL: %,d".format(totalRecords),
                                    color = HudPrimary,
                                    indicatorOn = false
                                )
                                HudBadge(
                                    text = "INDEXED",
                                    color = HudSecondary,
                                    indicatorOn = true
                                )
                                HudBadge(
                                    text = "OFFLINE",
                                    color = HudAmber,
                                    indicatorOn = false
                                )
                            }

                            Text(
                                text = "LATENCY: <1MS",
                                fontFamily = ShareTechMonoFontFamily,
                                fontSize = 10.sp,
                                color = HudTextMuted
                            )
                        }
                    }
                }

                // Futuristic Scanning Beam
                HudScanningBeam(color = HudPrimary)

                // Search Bar Input Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    HudFrameBox(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = if (searchQuery.isNotEmpty()) HudPrimary else HudBorder,
                        glowColor = HudPrimary,
                        cornerLength = 10.dp,
                        backgroundColor = HudSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (searchQuery.isNotEmpty()) HudPrimary else HudTextMuted,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.onSearchQueryChange(it) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 8.dp)
                                    .testTag("search_text_input"),
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = HudTextPrimary,
                                    fontSize = 15.sp,
                                    fontFamily = ShareTechMonoFontFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                cursorBrush = SolidColor(HudPrimary),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "ENTER QUERY (CODE, PHONE, CARD, NAME)...",
                                            color = HudTextMuted,
                                            fontSize = 12.sp,
                                            fontFamily = ShareTechMonoFontFamily
                                        )
                                    }
                                    innerTextField()
                                }
                            )

                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.onSearchQueryChange("") },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .testTag("clear_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = HudPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Search Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SearchFilter.values().forEach { filter ->
                        val isSelected = filter == activeFilter
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) HudPrimary.copy(alpha = 0.2f) else Color(0xFF07121C),
                                    RoundedCornerShape(2.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) HudPrimary else HudBorder,
                                    RoundedCornerShape(2.dp)
                                )
                                .clickable { viewModel.onFilterChange(filter) }
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                                .testTag("filter_chip_${filter.name}")
                        ) {
                            Text(
                                text = filter.label,
                                fontFamily = OrbitronFontFamily,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) HudPrimary else HudTextSecondary,
                                letterSpacing = 0.6.sp
                            )
                        }
                    }
                }

                // Query summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) {
                            "MATCHES FOUND: ${records.size} [FILTER: ${activeFilter.code}]"
                        } else {
                            "DISPLAYING ${records.size} RECENT RECORDS"
                        },
                        fontFamily = ShareTechMonoFontFamily,
                        fontSize = 11.sp,
                        color = if (records.isNotEmpty()) HudSecondary else HudAmber
                    )

                    if (totalRecords > 0) {
                        Text(
                            text = "DATABASE ONLINE",
                            fontFamily = ShareTechMonoFontFamily,
                            fontSize = 10.sp,
                            color = HudTextMuted
                        )
                    }
                }

                // Records List or Empty State
                if (records.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        HudFrameBox(
                            modifier = Modifier.fillMaxWidth(0.92f),
                            borderColor = HudBorder,
                            glowColor = HudPrimary.copy(alpha = 0.5f),
                            cornerLength = 14.dp,
                            backgroundColor = HudSurface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = HudPrimary.copy(alpha = 0.6f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (totalRecords == 0) "DATABASE VAULT EMPTY" else "NO MATCHING RECORDS",
                                    fontFamily = OrbitronFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HudPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (totalRecords == 0) {
                                        "Import a CSV file with citizen/account records or inject the tactical sample dataset to test instant search."
                                    } else {
                                        "No records matched query '${searchQuery}' under ${activeFilter.label}. Try a different term or select 'ALL FIELDS'."
                                    },
                                    fontFamily = ShareTechMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = HudTextSecondary,
                                    lineHeight = 16.sp
                                )

                                if (totalRecords == 0) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        HudButton(
                                            onClick = { viewModel.loadSampleData() },
                                            text = "LOAD DEMO DATA",
                                            accentColor = HudPrimary,
                                            modifier = Modifier.weight(1f),
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Dataset,
                                                    contentDescription = null,
                                                    tint = HudPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        )

                                        HudButton(
                                            onClick = { viewModel.openImportDialog() },
                                            text = "IMPORT CSV",
                                            accentColor = HudSecondary,
                                            modifier = Modifier.weight(1f),
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.FileUpload,
                                                    contentDescription = null,
                                                    tint = HudSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("records_lazy_list"),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = records,
                            key = { it.id }
                        ) { record ->
                            HudRecordCard(
                                record = record,
                                onInspectClick = { viewModel.selectRecord(it) },
                                onCopyField = { label, value ->
                                    viewModel.copyToClipboard(label, value)
                                }
                            )
                        }
                    }
                }
            }

            // Floating Toast Flash Banner at bottom
            AnimatedVisibility(
                visible = flashMessage != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                flashMessage?.let { msg ->
                    HudFrameBox(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        borderColor = HudPrimary,
                        glowColor = HudPrimary,
                        cornerLength = 8.dp,
                        backgroundColor = Color(0xFF04121E)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(HudSecondary, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = msg,
                                fontFamily = ShareTechMonoFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = HudPrimary
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs
    HudImportDialog(
        isOpen = isImportOpen,
        progress = importProgress,
        onDismiss = { viewModel.closeImportDialog() },
        onFileSelected = { uri -> viewModel.importCsv(uri) },
        onInjectSampleData = { viewModel.loadSampleData() }
    )

    HudStatsDialog(
        isOpen = isStatsOpen,
        totalRecords = totalRecords,
        onDismiss = { viewModel.closeStatsDialog() },
        onExportCsv = { viewModel.exportAndShareCsv(context) },
        onClearDatabase = { viewModel.clearDatabase() }
    )

    HudRecordDetailDialog(
        record = selectedRecord,
        onDismiss = { viewModel.selectRecord(null) },
        onCopyField = { label, value -> viewModel.copyToClipboard(label, value) },
        onCopyDossier = { rec -> viewModel.copyDossier(rec) },
        onShareRecord = { rec -> viewModel.shareRecord(context, rec) },
        onDeleteRecord = { id -> viewModel.deleteRecord(id) }
    )
}
