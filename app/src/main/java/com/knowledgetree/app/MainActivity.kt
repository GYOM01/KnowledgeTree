package com.knowledgetree.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.knowledgetree.app.ui.theme.KnowledgeTreeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KnowledgeTreeTheme { KnowledgeTreeApp() } }
    }
}

data class KnowledgeEntry(val title: String, val type: String, val subtitle: String)

@Composable
fun KnowledgeTreeApp() {
    var selected by remember { mutableIntStateOf(0) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf("首页" to Icons.Outlined.Home, "收藏" to Icons.Outlined.StarBorder, "记录" to Icons.Outlined.History, "设置" to Icons.Outlined.Settings)
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = { selected = index },
                        icon = { Icon(item.second, null) },
                        label = { Text(item.first) }
                    )
                }
            }
        }
    ) { padding ->
        when (selected) {
            0 -> HomeScreen(Modifier.padding(padding))
            else -> PlaceholderScreen(listOf("收藏", "学习记录", "设置")[selected - 1], Modifier.padding(padding))
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    var selectedEntry by remember { mutableStateOf<KnowledgeEntry?>(null) }
    val entries = listOf(
        KnowledgeEntry("摄影", "能力型", "沿完整拍摄流程学习"),
        KnowledgeEntry("法国大革命", "历史型", "沿时间线理解因果"),
        KnowledgeEntry("Python", "技能型", "从任务到可运行结果"),
        KnowledgeEntry("爵士乐", "分类型", "按流派、时期与听感探索")
    )

    if (selectedEntry != null) {
        EntryDetail(selectedEntry!!, onBack = { selectedEntry = null }, modifier)
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color(0xFFF8F5FF)),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("知识树", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("先判断知识类型，再决定怎样学", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                placeholder = { Text("搜索摄影、历史、编程……") },
                shape = RoundedCornerShape(20.dp)
            )
        }
        item { SectionTitle("推荐探索") }
        items(entries.filter { query.isBlank() || it.title.contains(query, true) }) { entry ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().clickable { selectedEntry = entry },
                shape = RoundedCornerShape(22.dp)
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                        Icon(Icons.Outlined.AccountTree, null, Modifier.padding(14.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(entry.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    AssistChip(onClick = { selectedEntry = entry }, label = { Text(entry.type) })
                }
            }
        }
    }
}

@Composable
private fun EntryDetail(entry: KnowledgeEntry, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val steps = when (entry.title) {
        "摄影" -> listOf("明确拍摄目标", "准备设备", "设置曝光参数", "完成构图", "对焦与拍摄", "检查并复盘")
        "法国大革命" -> listOf("旧制度背景", "1789年爆发", "君主制瓦解", "共和国与恐怖统治", "督政府", "历史影响")
        "Python" -> listOf("明确任务", "拆解输入输出", "写出最小代码", "运行与排错", "重构", "保存与复用")
        else -> listOf("起源", "主要流派", "代表人物", "听感特征", "经典作品", "延伸探索")
    }
    LazyColumn(modifier.fillMaxSize().background(Color(0xFFF8F5FF)), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "返回") }
            Text(entry.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text("${entry.type} · ${entry.subtitle}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(steps.mapIndexed { i, s -> "${i + 1}. $s" }) { step ->
            Card(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.RadioButtonUnchecked, null)
                    Spacer(Modifier.width(12.dp))
                    Text(step, Modifier.weight(1f), fontWeight = FontWeight.Medium)
                    Icon(Icons.Outlined.ChevronRight, null)
                }
            }
        }
    }
}

@Composable private fun SectionTitle(text: String) = Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

@Composable
private fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.Construction, null, Modifier.size(48.dp))
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Text("正式功能将在下一阶段接入")
        }
    }
}
