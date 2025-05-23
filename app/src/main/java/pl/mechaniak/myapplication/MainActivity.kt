package pl.mechaniak.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.mechaniak.myapplication.ui.theme.Beige
import pl.mechaniak.myapplication.ui.theme.Brown
import pl.mechaniak.myapplication.ui.theme.Chocolate
import pl.mechaniak.myapplication.ui.theme.DiceGameTheme
import pl.mechaniak.myapplication.ui.theme.PurpleHeader
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceGameApp()
                }
            }
        }
    }
}

@Composable
fun DiceGameApp() {
    var diceValues by remember { mutableStateOf(List(5) { 0 }) }
    var lastRollResult by remember { mutableStateOf(0) }
    var gameResult by remember { mutableStateOf(0) }
    var hasRolled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kości",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .fillMaxWidth()
                .background(PurpleHeader)
                .padding(8.dp)
        )

        Text(
            text = "Gra w kości. Autor 000000000",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Brown)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                diceValues = List(5) { Random.nextInt(1, 7) }
                hasRolled = true

                lastRollResult = calculateResult(diceValues)

                gameResult += lastRollResult
            },
            colors = ButtonDefaults.buttonColors(containerColor = Chocolate),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text("RZUĆ KOŚĆMI", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until 5) {
                val imageResource = when {
                    !hasRolled -> R.drawable.question
                    else -> when (diceValues[i]) {
                        1 -> R.drawable.dice_1
                        2 -> R.drawable.dice_2
                        3 -> R.drawable.dice_3
                        4 -> R.drawable.dice_4
                        5 -> R.drawable.dice_5
                        else -> R.drawable.dice_6
                    }
                }

                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Dice ${i+1}",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(9.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Wynik tego losowania: ${if (hasRolled) lastRollResult else 0}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Text(
            text = "Wynik gry: $gameResult",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 5.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                diceValues = List(5) { 0 }
                lastRollResult = 0
                gameResult = 0
                hasRolled = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = Chocolate),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text("RESETUJ WYNIK", color = Color.White)
        }
    }
}

fun calculateResult(diceValues: List<Int>): Int {
    val valueOccurrences = diceValues.groupBy { it }

    val repeatedValues = valueOccurrences.filter { it.value.size >= 2 }

    return repeatedValues.flatMap { it.value }.sum()
}